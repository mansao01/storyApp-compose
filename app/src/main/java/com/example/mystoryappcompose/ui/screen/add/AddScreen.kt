package com.example.mystoryappcompose.ui.screen.add

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystoryappcompose.R
import com.example.mystoryappcompose.ui.common.AddUiState
import com.example.mystoryappcompose.ui.component.LoadingScreen
import com.example.mystoryappcompose.ui.component.MToast
import com.example.mystoryappcompose.utils.CameraUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AddScreen(
    uiState: AddUiState,
    addViewModel: AddViewModel = viewModel(factory = AddViewModel.Factory),
    navigateToHome: () -> Unit
) {
    val context = LocalContext.current

    when (uiState) {
        is AddUiState.StandBy -> AddScreenComponent(addViewModel = addViewModel)
        is AddUiState.Loading -> LoadingScreen()
        is AddUiState.Success -> {
            LaunchedEffect(key1 = Unit) {
                Toast.makeText(context, uiState.postStoryResponse.message, Toast.LENGTH_SHORT)
                    .show()
            }
            navigateToHome()
        }

        is AddUiState.Error -> {
            MToast(context = context, message = uiState.msg)
            addViewModel.updateUiState()
        }
    }

}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AddScreenComponent(
    addViewModel: AddViewModel
) {
    val context = LocalContext.current
    val file = context.createImageFile()
    val cameraUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

    var galleryImageUri by remember { mutableStateOf<Uri?>(null) }
    var captureImageUri by remember { mutableStateOf<Uri?>(null) }
    var isImageSelected by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        galleryImageUri = uri
        isImageSelected = true

    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { isSuccess ->
        if (isSuccess) {
            captureImageUri = cameraUri
            isImageSelected = true

        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(cameraUri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        DisplaySelectedImage(imageUri = galleryImageUri ?: captureImageUri, context = context)
        if (isImageSelected) {
            Button(onClick = {
                captureImageUri = null
                galleryImageUri = null
                isImageSelected = false
            }) {
                Text(text = "Remove Image")
            }
        } else {
            Row {
                Button(onClick = {
                    openGallery(launcher = galleryLauncher)
                }) {
                    Text(text = "Open Gallery")
                }
                Button(onClick = {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(cameraUri)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }) {
                    Text(text = "Open Camera")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = "Description") },
            isError = description.isEmpty()
        )
        Spacer(modifier = Modifier.height(16.dp))

        UploadFile(
            imageUri = galleryImageUri ?: captureImageUri,
            description = description,
            addViewModel = addViewModel
        )
    }


}


@Composable
fun DisplaySelectedImage(imageUri: Uri?, context: Context) {

    if (imageUri != null) {
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        }
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.size(400.dp)
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_image),
            contentDescription = null,
            modifier = Modifier.size(400.dp)
        )
    }

}

@Composable
fun UploadFile(
    imageUri: Uri?,
    description: String,
    addViewModel: AddViewModel,
) {
    val context = LocalContext.current
    val isButtonEnable = (description.isNotEmpty() && imageUri != null)
    val myFile = imageUri?.let { CameraUtils.uriToFile(it, context) }
    val rotatedFile = myFile?.let { file ->
        CameraUtils.rotateFile(file)
    }

    Button(
        onClick = {
            rotatedFile?.let {
                addViewModel.postStory(it, description)
            }
        },
        enabled = isButtonEnable,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(text = "Upload")
    }
}


fun openGallery(launcher: ActivityResultLauncher<String>) {
    launcher.launch("image/*")
}


fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
}