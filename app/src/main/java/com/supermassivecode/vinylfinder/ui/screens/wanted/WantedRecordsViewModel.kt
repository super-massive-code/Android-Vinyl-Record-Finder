package com.supermassivecode.vinylfinder.ui.screens.wanted

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.data.local.WantedFoundRecordsRepository
import com.supermassivecode.vinylfinder.data.local.model.WantedRecordDTO
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
    private val repository: WantedFoundRecordsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<WantedRecordsUiState>(WantedRecordsUiState.Loading)
    val state: StateFlow<WantedRecordsUiState> = _state.asStateFlow()

    /**
     * TODO:
     * viewOnDiscogs
     * delete
     */

    init {
        loadWantedRecords()
    }

    fun loadWantedRecords() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = WantedRecordsUiState.Loading
                val records = repository.getAllWantedRecordsAsDTO()
                _state.value = WantedRecordsUiState.Success(data = records)
                //TODO what to do when we have no records? toast?
            } catch (e: Exception) {
                // Handle error - you might want to define specific error string resources
                // _state.value = WantedRecordsUiState.Error(R.string.error_loading_wanted_records)
            }
        }
    }

}