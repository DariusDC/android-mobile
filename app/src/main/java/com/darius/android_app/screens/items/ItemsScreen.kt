package com.darius.android_app.screens.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darius.android_app.AndroidApplication
import com.darius.android_app.R
import com.darius.android_app.widgets.ItemList
import com.darius.android_app.widgets.MyAppBar

@Composable
fun ItemsScreen(onItemClick: (id: String?) -> Unit, onAddItem: () -> Unit, onLogout: () -> Unit) {
    val itemsViewModel =
        viewModel<ItemsViewModel>(factory = ItemsViewModel.Factory(LocalContext.current.applicationContext as AndroidApplication))
    val itemsUIState = itemsViewModel.uiState
    val progressState = itemsViewModel.progressState

    Scaffold(
        topBar = {
            MyAppBar(
                isConnected = itemsViewModel.baseUIState,
                title = stringResource(id = R.string.items),
                moreActions = {
                    Button(onClick = onLogout) {
                        Text(text = stringResource(id = R.string.logout))
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddItem) {
                Icon(Icons.Rounded.Add, "Add")
            }
        }
    ) {
        when (itemsUIState) {
            is ItemsUIState.Success -> Column {
                LinearProgressIndicator(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    progress = progressState.progress * 0.1f
                )
                ItemList(
                    hotels = itemsUIState.hotels,
                    onItemClick = onItemClick
                )
            }
            is ItemsUIState.Loading -> CircularProgressIndicator()
            is ItemsUIState.Error -> Text(text = "Failed to load itemns - $it, ${itemsUIState.exception?.message}")
        }
    }
}

@Preview
@Composable
fun PreviewItemsScreen() {
    ItemsScreen(onItemClick = {}, onAddItem = {}, onLogout = {})
}