package com.supermassivecode.vinylfinder.ui.screens

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.R
import com.supermassivecode.vinylfinder.data.local.DiscogsRepository
import com.supermassivecode.vinylfinder.data.local.WantedRecordsRepository
import com.supermassivecode.vinylfinder.data.local.model.RecordInfo
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Error(@StringRes val alertStringId: Int) : DetailUiState
    data class Success(val data: RecordInfo) : DetailUiState
}


class RecordDetailViewModel(
    private val discogsRepository: DiscogsRepository,
    private val wantedRecordsRepository: WantedRecordsRepository
) : ViewModel() {

    private var _state = MutableLiveData<DetailUiState>()
    val state: LiveData<DetailUiState> = _state

    init {
        _state.postValue(DetailUiState.Loading)
    }

    fun getReleaseDetail(record: RecordInfo) {
        viewModelScope.launch {
            searchDiscogs(record)
        }
    }

    private suspend fun searchDiscogs(record: RecordInfo) {
        discogsRepository.releaseDetail(record).let {
            if (it.data != null) {
                _state.postValue(DetailUiState.Success(data = it.data))
            } else {
                _state.postValue(DetailUiState.Error(it.errorStringId!!))
            }
        }
    }

    fun addRecordToWatchList(recordInfo: RecordInfo) {
        viewModelScope.launch {
            val bob =  wantedRecordsRepository.getAll()
            wantedRecordsRepository.addRecord(recordInfo)
        }
    }

    //TODO: check if record is in list for whether to show/hide/colour the add button
}
