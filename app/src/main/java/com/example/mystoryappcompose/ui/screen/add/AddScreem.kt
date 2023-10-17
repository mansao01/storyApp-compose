package com.example.mystoryappcompose.ui.screen.add

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.mystoryappcompose.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun AddScreen() {
    AddScreenComponent()

}

@Composable
fun AddScreenComponent() {
    val context = LocalContext.current
    val file = context.createImageFile()
    val cameraUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

    var galleryImageUri by remember { mutableStateOf<Uri?>(null) }
    var captureImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        galleryImageUri = uri
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ){isSuccess ->
        if (isSuccess){
            captureImageUri = cameraUri
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
        Row {
            Button(onClick = { openGallery(launcher = galleryLauncher) }) {
                Text(text = "Open Gallery")
            }
            Button(onClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(cameraUri)
                } else {
                    // Request a permission
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Text(text = "Open Camera")
            }
        }
    }

}


@Composable
fun DisplaySelectedImage(imageUri: Uri?, context: Context) {

        if (imageUri != null) {
            val source = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.createSource(context.contentResolver, imageUri)
            } else {
                TODO("VERSION.SDK_INT < P")
            }
            val bitmap = ImageDecoder.decodeBitmap(source)

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