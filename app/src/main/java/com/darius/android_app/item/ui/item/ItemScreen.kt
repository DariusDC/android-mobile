package com.darius.android_app.item.ui.item

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.darius.android_app.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemScreen(context: Context, itemId: String?, onClose: () -> Unit) {

    val itemViewModel = viewModel<ItemViewModel>(factory = ItemViewModel.Factory(itemId))
    val itemUIState = itemViewModel.uiState
    var itemInputData by rememberSaveable { mutableStateOf(ItemInputData()) }

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

    LaunchedEffect(itemId, itemUIState.isLoading) {
        if (inputInit) {
            return@LaunchedEffect
        }
        if (itemUIState.hotel != null && !itemUIState.isLoading) {
            itemInputData = ItemInputData(itemUIState.hotel)
            inputInit = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.item)) },
                actions = {
                    Button(onClick = {
                        itemViewModel.saveOrUpdateItem(itemInputData)
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

        if (itemUIState.loadingError != null) {
            Text(text = "Failed to load item - ${itemUIState.loadingError.message}")
        }

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            AsyncImage(
                model = itemInputData.imageURL ?: "",
                contentDescription = "Hotel image",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )
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
                horizontalArrangement = Arrangement.SpaceBetween
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
            TextField(
                value = itemInputData.imageURL ?: "",
                onValueChange = {
                    itemInputData = itemInputData.copy(imageURL = it)
                },
                label = { Text("Image URL") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )
        }
    }

}

