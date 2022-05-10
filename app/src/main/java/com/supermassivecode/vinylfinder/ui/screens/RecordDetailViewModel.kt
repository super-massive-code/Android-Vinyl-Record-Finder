package com.supermassivecode.vinylfinder.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.data.local.DiscogsRepository
import com.supermassivecode.vinylfinder.data.local.WantedRecordsRepository
import com.supermassivecode.vinylfinder.data.local.model.RecordInfo
import kotlinx.coroutines.launch

class RecordDetailViewModel(
    private val discogsRepository: DiscogsRepository,
    private val wantedRecordsRepository: WantedRecordsRepository
): ViewModel() {

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _record = MutableLiveData<RecordInfo>()
    val record: LiveData<RecordInfo> = _record

    fun getReleaseDetail(record: RecordInfo) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            searchDiscogs(record)
            _isLoading.postValue(false)
        }
    }

    private suspend fun searchDiscogs(record: RecordInfo) {
        discogsRepository.releaseDetail(record).let {
            _record.postValue(it)
        }
    }

    fun addRecordToWatchList(recordInfo: RecordInfo) {
        wantedRecordsRepository.addRecord(recordInfo)
    }

    //TODO: check if record is in list for whether to show/hide/colour the add button
}
