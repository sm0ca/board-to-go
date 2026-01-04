package com.example.boardtogo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

import kotlin.time.Duration.Companion.minutes

private const val INIT_TIMESTAMP = "0000-01-01 00:00:00"

class DeparturesViewModel: ViewModel() {
    var snackbarMessage by mutableStateOf<String?>(null)
    var isRefreshing by mutableStateOf(false)
    private var dataTimeStamp by mutableStateOf(INIT_TIMESTAMP)
    var allTrips by mutableStateOf<List<Trip>?>(null)

    init {
        flow {
            while (true) {
                emit(Unit)
                delay(2.minutes)
            }
        }.onEach {
            getDepartures()
        }.launchIn(viewModelScope)
    }

    suspend fun getDepartures() {
        try {
            val response = RetrofitClient.goAPIService.getUnionDepartures()
            val info = response.metadataInfo
            val trips = response.allDepartures?.trip

            if (info == null || trips == null || info.errorCode != "200") return

            if (info.timeStamp > dataTimeStamp) {
                dataTimeStamp = info.timeStamp
                allTrips = trips.sortedBy { it.time }
            }
        } catch (_: Exception) {
            showSnackbar()
            allTrips = allTrips.orEmpty()
        }
    }

    fun refreshDepartures() {
        viewModelScope.launch {
            isRefreshing = true
            getDepartures()
            // To ensure the refresh indicator is displayed/dismissed properly
            delay(500)
            isRefreshing = false
        }
    }

    fun showSnackbar() {
        snackbarMessage = "Error: Unable to fetch new data... (╥_╥)"
    }

    fun resetSnackbar() {
        snackbarMessage = null
    }
}
