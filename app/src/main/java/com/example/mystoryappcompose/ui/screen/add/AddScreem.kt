package com.example.mystoryappcompose.ui.screen.add

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
import com.example.mystoryappcompose.R


@Composable
fun AddScreen() {
    AddScreenComponent()

}

@Composable
fun AddScreenComponent() {
    val context = LocalContext.current
    var galleryImageUri by remember { mutableStateOf<Uri?>(null)}
    var captureImageUri by remember{ mutableStateOf<Uri>(Uri.EMPTY) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        galleryImageUri = uri
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        DisplaySelectedImage(imageUri = galleryImageUri, context = context)
        Row {
            Button(onClick = { openGallery(launcher = galleryLauncher) }) {
                Text(text = "Open Gallery")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Open Camera")
            }
        }
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

        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(400.dp)
            )
        }
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
