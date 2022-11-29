package com.darius.android_app.item.ui.items

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darius.android_app.R

@Composable
fun ItemsScreen(onItemClick: (id: String?) -> Unit, onAddItem: () -> Unit, onLogout: () -> Unit) {
    val itemsViewModel = viewModel<ItemsViewModel>(factory = ItemsViewModel.Factory)
    val itemsUIState = itemsViewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.items)) },
                actions = {
                    Button(onClick = onLogout) {
                        Text(text = stringResource(id = R.string.logout))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddItem) {
                Icon(Icons.Rounded.Add, "Add")
            }
        }
    ) {
        when (itemsUIState) {
            is ItemsUIState.Success -> ItemList(
                hotels = itemsUIState.hotels,
                onItemClick = onItemClick
            )
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