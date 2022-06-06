package com.supermassivecode.vinylfinder.ui.screens.search

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.data.local.DiscogsRepository
import com.supermassivecode.vinylfinder.data.local.WantedFoundRecordsRepository
import com.supermassivecode.vinylfinder.data.local.model.RecordInfoDTO
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

    private var _state = MutableLiveData<DetailUiState>()
    val state: LiveData<DetailUiState> = _state

    init {
        _state.postValue(DetailUiState.Loading)
    }

    fun getReleaseDetail(record: RecordInfoDTO) {
        viewModelScope.launch {
            searchDiscogs(record)
        }
    }

    private suspend fun searchDiscogs(record: RecordInfoDTO) {
        discogsRepository.releaseDetail(record).let {
            if (it.data != null) {
                _state.postValue(DetailUiState.Success(
                    data = it.data,
                    inWatchList = wantedFoundRecordsRepository.wantedRecordExistsInDatabase(it.data))
                )
            } else {
                _state.postValue(DetailUiState.Error(it.errorStringId!!))
            }
        }
    }

    fun toggleRecordInWatchList(recordInfoDTO: RecordInfoDTO) {
        viewModelScope.launch {
            if (wantedFoundRecordsRepository.wantedRecordExistsInDatabase(recordInfoDTO)) {
                wantedFoundRecordsRepository.removeWantedRecord(recordInfoDTO)
                _state.postValue(DetailUiState.Success(data = recordInfoDTO, inWatchList = false))
            } else {
                wantedFoundRecordsRepository.addWantedRecord(recordInfoDTO)
                _state.postValue(DetailUiState.Success(data = recordInfoDTO, inWatchList = true))
            }
        }
    }
}
