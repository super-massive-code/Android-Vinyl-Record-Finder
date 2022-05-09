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

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _records = MutableLiveData<List<RecordInfo>>()
    val records: LiveData<List<RecordInfo>> = _records

    init {
        search("Bob")
    }

    fun search(query: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            searchDiscogs(query)
            _isLoading.postValue(false)
        }
    }

    private suspend fun searchDiscogs(query: String) {
        //TODO: get result code from repository for network offline / 500 etc?
        discogsRepository.search(query).let {
            _records.postValue(it)
        }
    }
}
