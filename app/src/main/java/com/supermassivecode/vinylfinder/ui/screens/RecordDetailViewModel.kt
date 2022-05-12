package com.supermassivecode.vinylfinder.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.R
import com.supermassivecode.vinylfinder.data.local.DiscogsRepository
import com.supermassivecode.vinylfinder.data.local.WantedRecordsRepository
import com.supermassivecode.vinylfinder.data.local.model.RecordInfo
import kotlinx.coroutines.launch

class RecordDetailViewModel(
    private val discogsRepository: DiscogsRepository,
    private val wantedRecordsRepository: WantedRecordsRepository
): ViewModel() {

    private var _state = MutableLiveData<UiState<RecordInfo>>()
    val state: LiveData<UiState<RecordInfo>> = _state

    fun getReleaseDetail(record: RecordInfo) {
        viewModelScope.launch {
            searchDiscogs(record)
        }
    }

    private suspend fun searchDiscogs(record: RecordInfo) {
        _state.postValue(UiState(isLoading = true))
        discogsRepository.releaseDetail(record).let {
            _state.postValue(UiState(data = it.data, alertStringId = it.errorStringId))
        }
    }

    fun addRecordToWatchList(recordInfo: RecordInfo) {
        wantedRecordsRepository.addRecord(recordInfo)
    }

    //TODO: check if record is in list for whether to show/hide/colour the add button
}
