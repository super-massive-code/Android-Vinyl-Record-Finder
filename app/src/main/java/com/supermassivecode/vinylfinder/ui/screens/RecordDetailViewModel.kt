package com.supermassivecode.vinylfinder.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.data.local.DiscogsRepository
import com.supermassivecode.vinylfinder.data.local.model.RecordInfo
import kotlinx.coroutines.launch

class RecordDetailViewModel(
    private val discogsRepository: DiscogsRepository
): ViewModel() {

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _record = MutableLiveData<RecordInfo>()
    val record: LiveData<RecordInfo> = _record

    fun getReleaseDetail(record: RecordInfo) {
        viewModelScope.launch {
            _isLoading.value = true
            searchDiscogs(record)
            _isLoading.value = false
        }
    }

    private suspend fun searchDiscogs(record: RecordInfo) {
        discogsRepository.releaseDetail(record).let {
            _record.value = it
        }
    }
}