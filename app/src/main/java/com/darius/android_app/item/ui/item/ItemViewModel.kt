package com.darius.android_app.item.ui.item

import android.os.Parcelable
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
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.lang.Exception

data class HotelUIState(
    val isLoading: Boolean = false,
    val loadingError: Throwable? = null,
    val itemId: String? = null,
    val hotel: Hotel? = null,
    val isSaving: Boolean = false,
    val savingComplete: Boolean = false,
    val savingError: Throwable? = null,
)

class ItemViewModel(private val itemId: String?, private val hotelRepository: HotelRepository) :
    ViewModel() {

    var uiState: HotelUIState by mutableStateOf(HotelUIState(isLoading = true))
        private set

    init {
        if (itemId != null) {
            loadItem()
        } else {
            uiState = uiState.copy(hotel = Hotel(), isLoading = false)
        }
    }

    private fun loadItem() {
        viewModelScope.launch {
            hotelRepository.itemStream.collect { items ->
                if (!uiState.isLoading) {
                    return@collect
                }
                val item = items.find { it._id == itemId }
                uiState = uiState.copy(hotel = item, isLoading = false)
            }
        }
    }

    fun saveOrUpdateItem(itemInputData: ItemInputData) {
        viewModelScope.launch {
            val hotel = Hotel(
                itemId ?: "",
                itemInputData.name ?: "",
                itemInputData.price.toDoubleOrNull() ?: 0.0,
                itemInputData.description ?: "",
                itemInputData.added ?: "",
                itemInputData.available ?: false,
                itemInputData.imageURL ?: ""
            )
            try {
                uiState = uiState.copy(isSaving = true, savingError = null)
                if (itemId == null) {
                    hotelRepository.save(hotel)
                } else {
                    hotelRepository.update(hotel)
                }
                uiState = uiState.copy(isSaving = false, savingComplete = true)
            } catch (e: Exception) {
                uiState = uiState.copy(isSaving = false, savingError = e)
            }

        }
    }

    companion object {
        fun Factory(itemId: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AndroidApplication)
                ItemViewModel(itemId, app.container.hotelRepository)
            }
        }
    }
}

@Parcelize
data class ItemInputData(
    val name: String = "",
    val description: String = "",
    val available: Boolean = false,
    val added: String? = "",
    val imageURL: String? = "",
    val price: String = ""
) : Parcelable {
    constructor(hotel: Hotel) : this(
        hotel.name, hotel.desc, hotel.available, hotel.added,
        hotel.imageURL, hotel.price.toString()
    )
}