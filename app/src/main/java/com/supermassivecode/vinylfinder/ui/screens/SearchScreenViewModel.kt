package com.supermassivecode.vinylfinder.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.R
import com.supermassivecode.vinylfinder.data.local.DiscogsRepository
import com.supermassivecode.vinylfinder.data.local.model.RecordInfo
import kotlinx.coroutines.launch

class SearchScreenViewModel(
    private val discogsRepository: DiscogsRepository
) : ViewModel() {

    private var _state = MutableLiveData<UiState<List<RecordInfo>>>()
    val state: LiveData<UiState<List<RecordInfo>>> = _state

    init {
        search("Bob")
    }

    fun search(query: String) {
        viewModelScope.launch {
            searchDiscogs(query)
        }
    }

    private suspend fun searchDiscogs(query: String) {
        _state.postValue(UiState(isLoading = true))
        discogsRepository.search(query).let {
            _state.postValue(UiState(data = it.data, alertStringId = it.errorStringId))
        }
    }
}
