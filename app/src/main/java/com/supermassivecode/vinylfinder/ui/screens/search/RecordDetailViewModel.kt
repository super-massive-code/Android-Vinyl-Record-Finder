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
    data class Success(val data: RecordInfoDTO) : DetailUiState
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
                _state.postValue(DetailUiState.Success(data = it.data))
            } else {
                _state.postValue(DetailUiState.Error(it.errorStringId!!))
            }
        }
    }

    fun addRecordToWatchList(recordInfoDTO: RecordInfoDTO) {
        viewModelScope.launch {
            val bob =  wantedFoundRecordsRepository.getAllWantedRecords()
            wantedFoundRecordsRepository.addWantedRecord(recordInfoDTO)
        }
    }

    //TODO: check if record is in list for whether to show/hide/colour the add button
}
