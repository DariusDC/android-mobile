package com.darius.android_app.item.ui.items

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.darius.android_app.AndroidApplication
import com.darius.android_app.item.data.Hotel
import com.darius.android_app.item.data.HotelRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed interface ItemsUIState {
    data class Success(val hotels: List<Hotel>) : ItemsUIState
    data class Error(val exception: Throwable?) : ItemsUIState
    object Loading : ItemsUIState
}

class ItemsViewModel(private val hotelRepository: HotelRepository) : ViewModel() {
    var uiState: ItemsUIState by mutableStateOf(ItemsUIState.Loading)

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            uiState = ItemsUIState.Loading
            hotelRepository.refresh()
            hotelRepository.itemStream.collect {
                uiState = ItemsUIState.Success(it)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AndroidApplication)
                ItemsViewModel(app.container.hotelRepository)
            }
        }
    }
}