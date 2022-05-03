package com.supermassivecode.vinylfinder.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.data.local.DiscogsRepository
import com.supermassivecode.vinylfinder.data.local.model.RecordInfo
import kotlinx.coroutines.launch

class SearchScreenViewModel(
    private val discogsRepository: DiscogsRepository
): ViewModel() {

    private var _records = MutableLiveData<List<RecordInfo>>()
    private var _isLoading = MutableLiveData(false)

    val records: LiveData<List<RecordInfo>> = _records
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        search("Bob")
    }

    fun search(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            searchDiscogs(query)
            _isLoading.value = false
        }
    }

    private suspend fun searchDiscogs(query: String) {
        //TODO: get result code from repository for network offline / 500 etc?
        discogsRepository.search(query).let {
            _records.value = it
        }
    }
}
