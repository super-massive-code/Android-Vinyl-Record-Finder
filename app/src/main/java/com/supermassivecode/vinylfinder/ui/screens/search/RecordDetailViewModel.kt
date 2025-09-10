package com.supermassivecode.vinylfinder.ui.screens.search

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.data.local.DiscogsRepository
import com.supermassivecode.vinylfinder.data.local.WantedFoundRecordsRepository
import com.supermassivecode.vinylfinder.data.local.model.RecordInfoDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Error(@StringRes val alertStringId: Int) : DetailUiState
    data class Success(val data: RecordInfoDTO, val inWatchList: Boolean) : DetailUiState
}

class RecordDetailViewModel(
    private val discogsRepository: DiscogsRepository,
    private val wantedFoundRecordsRepository: WantedFoundRecordsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    fun getReleaseDetail(record: RecordInfoDTO) {
        viewModelScope.launch {
            _state.value = DetailUiState.Loading
            searchDiscogs(record)
        }
    }

    private suspend fun searchDiscogs(record: RecordInfoDTO) {
        discogsRepository.releaseDetail(record).let {
            if (it.data != null) {
                _state.value = DetailUiState.Success(
                    data = it.data,
                    inWatchList = wantedFoundRecordsRepository.wantedRecordExistsInDatabase(it.data)
                )
            } else {
                _state.value = DetailUiState.Error(it.errorStringId!!)
            }
        }
    }

    fun toggleRecordInWatchList(recordInfoDTO: RecordInfoDTO) {
        viewModelScope.launch {
            if (wantedFoundRecordsRepository.wantedRecordExistsInDatabase(recordInfoDTO)) {
                wantedFoundRecordsRepository.removeWantedRecord(recordInfoDTO)
                _state.value = DetailUiState.Success(data = recordInfoDTO, inWatchList = false)
            } else {
                wantedFoundRecordsRepository.addWantedRecord(recordInfoDTO)
                _state.value = DetailUiState.Success(data = recordInfoDTO, inWatchList = true)
            }
        }
    }
}