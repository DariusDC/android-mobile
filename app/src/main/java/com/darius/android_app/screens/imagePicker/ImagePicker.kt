package com.darius.android_app.screens.imagePicker

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.darius.android_app.utils.camera.ComposeFileProvider

@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
    closeModal: () -> Unit,
    onImageChoose: (Uri) -> Unit
) {
    val context = LocalContext.current

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
            imageUri?.let { onImageChoose(it) }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {
            imageUri?.let { onImageChoose(it) }
        }
    )

    Box(modifier = modifier) {
        AlertDialog(onDismissRequest = { closeModal() },
            title = { Text(text = "Hotel image") },
            text = {
                Column {
                    Text(text = "How do you want to add the image?")
                }
            }, buttons = {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    Button(onClick = {
                        val uri = ComposeFileProvider.getImageUri(context)
                        imageUri = uri
                        cameraLauncher.launch(uri)
                    }, modifier = Modifier.padding(horizontal = 10.dp)) {
                        Text(text = "Take a photo")
                    }
                    Button(onClick = {
                        imagePicker.launch("image/*")
                    }, modifier = Modifier.padding(end = 10.dp)) {
                        Text(text = "Take from gallery")
                    }
                }
            })
    }
}