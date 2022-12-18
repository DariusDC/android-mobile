package com.darius.android_app.screens.item

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.darius.android_app.AndroidApplication
import com.darius.android_app.R
import com.darius.android_app.screens.imagePicker.ImagePicker
import com.darius.android_app.utils.camera.Base64Utils
import com.darius.android_app.utils.showSimpleNotificationWithTapAction
import com.darius.android_app.widgets.MyAppBar
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemScreen(context: Context, itemId: String?, onClose: () -> Unit) {

    val itemViewModel = viewModel<ItemViewModel>(
        factory = ItemViewModel.Factory(
            itemId,
            LocalContext.current.applicationContext as AndroidApplication
        )
    )
    val itemUIState = itemViewModel.uiState
    var itemInputData by rememberSaveable { mutableStateOf(ItemInputData()) }
    val channelId = "MyTestChannel"
    val notificationId = 0
    val dialogOpened = remember { mutableStateOf(false) }

    LaunchedEffect(itemUIState.savingComplete) {
        if (itemUIState.savingComplete) {
            onClose()
        }
    }

    var inputInit by remember { mutableStateOf(itemId == null) }
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            itemInputData =
                itemInputData.copy(
                    added = LocalDateTime.of(mYear, mMonth + 1, mDayOfMonth, 0, 0, 0, 0).format(
                        DateTimeFormatter.ISO_DATE_TIME
                    )
                )
        }, 2022, 12, 1
    )
    var hotelImage = remember {
        mutableStateOf<Bitmap?>(null)
    }

    LaunchedEffect(itemId, itemUIState.isLoading) {
        if (inputInit) {
            return@LaunchedEffect
        }
        if (itemUIState.hotel != null && !itemUIState.isLoading) {
            itemInputData = ItemInputData(itemUIState.hotel)
            val b: ByteArray = Base64.getDecoder().decode(itemInputData.imageUri)
            hotelImage.value = BitmapFactory.decodeByteArray(b, 0, b.size)
            inputInit = true
        }
    }

    Scaffold(
        topBar = {
            MyAppBar(
                isConnected = itemViewModel.baseUIState,
                title = stringResource(id = R.string.item),
                moreActions = {
                    Button(onClick = {
                        itemViewModel.saveOrUpdateItem(itemInputData)
                        if (itemUIState.savingError == null) {
                            showSimpleNotificationWithTapAction(
                                context, channelId, notificationId,
                                "Hotel", "A new hotel has been added. Tap to see it"
                            )
                        }
                    }) {
                        Text(text = "Save")
                    }
                }
            )
        }
    ) {
        if (itemUIState.isLoading) {
            CircularProgressIndicator()
            return@Scaffold
        }

        if (dialogOpened.value) {
            ImagePicker(closeModal = { dialogOpened.value = false }, onImageChoose = {
                dialogOpened.value = false
                val inputStream = context.contentResolver.openInputStream(it)
                inputStream?.let { f ->
                    itemInputData.imageUri = Base64.getEncoder().encodeToString(f.readBytes())
                    val b: ByteArray = Base64.getDecoder().decode(itemInputData.imageUri)
                    hotelImage.value = BitmapFactory.decodeByteArray(b, 0, b.size)
                }
            })
        }

        if (itemUIState.loadingError != null) {
            Text(text = "Failed to load item - ${itemUIState.loadingError.message}")
        }

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            itemInputData.imageUri?.let {
                hotelImage.value?.asImageBitmap()?.let { it1 ->
                    Image(
                        contentDescription = "Hotel image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp), bitmap = it1
                    )
                }
            }
            TextField(
                value = itemInputData.name,
                label = { Text("Name") },
                onValueChange = { itemInputData = itemInputData.copy(name = it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )
            TextField(
                value = itemInputData.description,
                label = { Text("Description") },
                onValueChange = { itemInputData = itemInputData.copy(description = it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )
            TextField(
                value = itemInputData.price,
                onValueChange = {
                    itemInputData = itemInputData.copy(price = it)
                },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(onClick = { datePickerDialog.show() }) {
                    Text(text = "Added date")
                }
                if (itemInputData.added != "") {
                    Text(text = itemInputData.added ?: "")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
            ) {
                Text(text = "Hotel available")
                Box(modifier = Modifier.width(20.dp))
                Checkbox(checked = itemInputData.available, onCheckedChange = {
                    itemInputData = itemInputData.copy(available = it)
                })
            }
            Button(
                onClick = {
                    dialogOpened.value = true
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Text(text = "Change photo")
            }
        }
    }
}

