package com.supermassivecode.vinylfinder.ui.screens.search

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.data.local.DiscogsRepository
import com.supermassivecode.vinylfinder.data.local.model.RecordInfoDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SearchUiState {
    object Loading : SearchUiState
    data class Error(@StringRes val alertStringId: Int) : SearchUiState
    data class Success(val data: List<RecordInfoDTO>) : SearchUiState
}

class SearchScreenViewModel(
    private val discogsRepository: DiscogsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<SearchUiState>(SearchUiState.Success(emptyList()))
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    fun search(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchDiscogs(query)
        }
    }

    private suspend fun searchDiscogs(query: String) {
        _state.value = SearchUiState.Loading
        discogsRepository.search(query).let {
            if (it.data != null) {
                _state.value = SearchUiState.Success(data = it.data)
            } else {
                _state.value = SearchUiState.Error(it.errorStringId!!)
            }
        }
    }
}