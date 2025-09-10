package com.supermassivecode.vinylfinder.ui.screens.wanted

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.data.local.WantedFoundRecordsRepository
import com.supermassivecode.vinylfinder.data.local.model.FoundRecordDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface FoundSellersUiState {
    data class ShowFound(val data: List<FoundRecordDTO>) : FoundSellersUiState
    data class LoadWebView(val url: String) : FoundSellersUiState
}

class FoundSellersViewModel(
    private val foundRecordsRepository: WantedFoundRecordsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<FoundSellersUiState>(FoundSellersUiState.ShowFound(emptyList()))
    val state: StateFlow<FoundSellersUiState> = _state.asStateFlow()

    fun loadFound(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = FoundSellersUiState.ShowFound(foundRecordsRepository.getFoundRecordsForParent(uid))
        }
    }

    fun loadUrl(url: String) {
        _state.value = FoundSellersUiState.LoadWebView(url)
    }
}