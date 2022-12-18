package com.darius.android_app.screens.items

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.*
import com.darius.android_app.AndroidApplication
import com.darius.android_app.model.Hotel
import com.darius.android_app.repo.HotelRepository
import com.darius.android_app.screens.BaseViewModel
//import com.darius.android_app.utils.MyWorker
import com.darius.android_app.utils.MyWorkerLoading
import kotlinx.coroutines.launch
import java.time.temporal.ValueRange
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

sealed interface ItemsUIState {
    data class Success(val hotels: List<Hotel>, val progress: Int = 0) : ItemsUIState
    data class Error(val exception: Throwable?) : ItemsUIState
    object Loading : ItemsUIState
}

data class MyJobState(val progress: Int = 0)

class ItemsViewModel(
    private val hotelRepository: HotelRepository,
    application: AndroidApplication
) : BaseViewModel(application) {
    var uiState: ItemsUIState by mutableStateOf(ItemsUIState.Loading)
    var progressState: MyJobState by mutableStateOf(MyJobState())

    init {
//        startJob()
        startJobTest()
        loadItems()
    }

    private fun startJobTest() {
        viewModelScope.launch {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val myWork = OneTimeWorkRequest.Builder(MyWorkerLoading::class.java)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(getApplication()).enqueue(myWork)
            WorkManager.getInstance(getApplication())
                .getWorkInfoByIdLiveData(myWork.id)
                .observeForever { workInfo ->
//                    if (workInfo.state == WorkInfo.State.ENQUEUED) {
                        val progress = workInfo.progress.getInt("progress", 0)
                        print(progress)
                        progressState = progressState.copy(progress = progress)
//                    }
                }
        }
    }

//    private fun startJob() {
//        val vm = this
//        viewModelScope.launch {
//            val constraints = Constraints.Builder()s
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build()
//            val myPeriodicWork = PeriodicWorkRequestBuilder<MyWorker>(10, TimeUnit.SECONDS)
//                .setConstraints(constraints)
//                .build()
//
//            WorkManager.getInstance(getApplication()).enqueueUniquePeriodicWork(
//                "MyPeriodicWork",
//                ExistingPeriodicWorkPolicy.KEEP,
//                myPeriodicWork,
//            )
//            WorkManager.getInstance()
//                .getWorkInfoByIdLiveData(myPeriodicWork.id)
//                .observeForever { workInfo ->
//                    if ((workInfo != null) && (workInfo.state == WorkInfo.State.ENQUEUED)) {
//                        val myOutputData = workInfo.outputData.getString("items")
//                    }
//                }
//        }
//    }

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
        fun Factory(application: AndroidApplication): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AndroidApplication)
                ItemsViewModel(app.container.hotelRepository, application)
            }
        }
    }
}