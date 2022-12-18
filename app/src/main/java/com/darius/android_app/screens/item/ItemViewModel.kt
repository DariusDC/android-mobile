package com.darius.android_app.screens.item

import ServerWorker
import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.*
import com.darius.android_app.AndroidApplication
import com.darius.android_app.model.Hotel
import com.darius.android_app.repo.HotelRepository
import com.darius.android_app.screens.BaseViewModel
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

class ItemViewModel(
    private val itemId: String?,
    private val hotelRepository: HotelRepository,
    application: AndroidApplication
) :
    BaseViewModel(application) {

    private val workManager = WorkManager.getInstance(getApplication())
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
                "",
                itemInputData.imageUri
            )
            try {
                uiState = uiState.copy(isSaving = true, savingError = null)

                if (baseUIState) {
                    if (itemId == null) {
                        hotelRepository.save(hotel)
                    } else {
                        hotelRepository.update(hotel)
                    }
                } else {
                    if (itemId == null) {
                        hotelRepository.handleItemCreate(hotel)
                    } else {
                        hotelRepository.handleUpdatedItem(hotel)
                    }

                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED).build()

                    val inputData = Data.Builder()
                        .putBoolean("isSaving", itemId == null)
                        .putString("id", hotel._id)
                        .putString("name", hotel.name)
                        .putDouble("price", hotel.price)
                        .putString("desc", hotel.desc)
                        .putString("added", hotel.added)
                        .putBoolean("available", hotel.available)
                        .build()

                    val worker = OneTimeWorkRequest.Builder(ServerWorker::class.java)
                        .setConstraints(constraints)
                        .setInputData(inputData)
                        .build()


                    workManager.enqueue(worker);


                }

                uiState = uiState.copy(isSaving = false, savingComplete = true)
            } catch (e: Exception) {
                uiState = uiState.copy(isSaving = false, savingError = e)
            }

        }
    }

    companion object {
        fun Factory(itemId: String?, application: AndroidApplication): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val app =
                        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AndroidApplication)
                    ItemViewModel(itemId, app.container.hotelRepository, application)
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
    var imageUri: String? = null,
    val price: String = ""
) : Parcelable {
    constructor(hotel: Hotel) : this(
        hotel.name, hotel.desc, hotel.available, hotel.added,
        hotel.imageUri, hotel.price.toString()
    )
}