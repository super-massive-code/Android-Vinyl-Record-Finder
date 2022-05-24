package com.supermassivecode.vinylfinder.ui.screens.wanted

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.data.local.WantedFoundRecordsRepository
import com.supermassivecode.vinylfinder.data.local.model.FoundRecordDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface FoundSellersUiState {
    data class ShowFound(val data: List<FoundRecordDTO>) : FoundSellersUiState
    data class LoadWebView(val url: String) : FoundSellersUiState
}

class FoundSellersViewModel(
    private val foundRecordsRepository: WantedFoundRecordsRepository
) : ViewModel() {

    private val _state = MutableLiveData<FoundSellersUiState>()
    val state: LiveData<FoundSellersUiState> = _state

    init {
        _state.postValue(FoundSellersUiState.ShowFound(emptyList()))
    }

    fun loadFound(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
           _state.postValue(FoundSellersUiState.ShowFound(foundRecordsRepository.getFoundRecordsForParent(uid)))
        }
    }

    fun loadUrl(url: String) {
        _state.postValue(FoundSellersUiState.LoadWebView(url))
    }
}
