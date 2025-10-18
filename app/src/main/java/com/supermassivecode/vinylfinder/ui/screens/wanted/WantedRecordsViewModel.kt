package com.supermassivecode.vinylfinder.ui.screens.wanted

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    data class Error(@StringRes val alertStringId: Int) : WantedRecordsUiState
    data class Success(val data: List<WantedRecordDTO>) : WantedRecordsUiState
}

class WantedRecordsViewModel(
    private val wantedFoundRecordsRepository: WantedFoundRecordsRepository,
    private val discogsWantedSearch: DiscogsWantedSearch
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
                _state.value = WantedRecordsUiState.Success(data = records)
            } catch (e: Exception) {}
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
            print("")
            val searchResults = discogsWantedSearch.search(wantedFoundRecordsRepository.getAllWithMaxPriceSet())
            print("")
            searchResults.found.forEach { (wantedRecord, foundRecords) ->
                foundRecords.forEach { found ->
                    wantedFoundRecordsRepository.addFoundRecordIfNotExists(
                        wantedRecord.uid,
                        found,
                    )
                }
            }

            searchResults.exception?.let { exception ->
                // TODO: log exception with partial results count: ${searchResults.found.size} records processed
            }

            loadWantedRecords()
        }
    }
}