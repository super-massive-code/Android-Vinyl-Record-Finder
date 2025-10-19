package com.supermassivecode.vinylfinder.ui.screens.wanted

import TimestampManager
import android.provider.Settings.Global.getString
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.Logger
import com.supermassivecode.vinylfinder.R
import com.supermassivecode.vinylfinder.data.local.WantedFoundRecordsRepository
import com.supermassivecode.vinylfinder.data.local.model.WantedRecordDTO
import com.supermassivecode.vinylfinder.data.remote.discogs.DiscogsWantedSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface WantedRecordsUiState {
    object Loading : WantedRecordsUiState
    data class Error(@StringRes val stringId: Int) : WantedRecordsUiState
    data class Success(val data: List<WantedRecordDTO>,
                       val lastUpdateMessage: String) : WantedRecordsUiState
}

class WantedRecordsViewModel(
    private val wantedFoundRecordsRepository: WantedFoundRecordsRepository,
    private val discogsWantedSearch: DiscogsWantedSearch,
    private val timestampManager: TimestampManager
) : ViewModel() {

    private val _state = MutableStateFlow<WantedRecordsUiState>(WantedRecordsUiState.Loading)
    val state: StateFlow<WantedRecordsUiState> = _state.asStateFlow()

    init {
        loadWantedRecords()
    }

    fun loadWantedRecords() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = WantedRecordsUiState.Loading
                val records = wantedFoundRecordsRepository.getAllWantedRecordsAsDTO()
                val lastUpdateMessage = "Last checked: ${timestampManager.getLastPriceCheckFormatted() ?: "-"}"
                _state.value = WantedRecordsUiState.Success(
                    data = records,
                    lastUpdateMessage = lastUpdateMessage)
            } catch (e: Exception) {
                Logger.logException(e)
                _state.value = WantedRecordsUiState.Error(R.string.wanted_record_load_error)
            }
        }
    }

    fun deleteWantedRecord(discogsRemoteId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            wantedFoundRecordsRepository.removeWantedRecord(discogsRemoteId)
            loadWantedRecords()
        }
    }

    fun searchDiscogsForWantedRecords() {
        _state.value = WantedRecordsUiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val searchResults = discogsWantedSearch.search(wantedFoundRecordsRepository.getAllWithMaxPriceSet())
            searchResults.found.forEach { (wantedRecord, foundRecords) ->
                foundRecords.forEach { found ->
                    wantedFoundRecordsRepository.addFoundRecordIfNotExists(
                        wantedRecord.uid,
                        found,
                    )
                }
            }

            searchResults.exception?.let { exception ->
                Logger.logException(exception, "Found count: ${searchResults.found.size}")
            }

            timestampManager.stampRecordPriceCheck()
            loadWantedRecords()
        }
    }
}