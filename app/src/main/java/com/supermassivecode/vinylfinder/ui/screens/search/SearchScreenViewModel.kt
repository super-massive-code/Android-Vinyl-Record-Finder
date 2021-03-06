package com.supermassivecode.vinylfinder.ui.screens.search

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.data.local.DiscogsRepository
import com.supermassivecode.vinylfinder.data.local.model.RecordInfoDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface SearchUiState {
    object Loading : SearchUiState
    data class Error(@StringRes val alertStringId: Int) : SearchUiState
    data class Success(val data: List<RecordInfoDTO>) : SearchUiState
}

class SearchScreenViewModel(
    private val discogsRepository: DiscogsRepository,
) : ViewModel() {

    private var _state = MutableLiveData<SearchUiState>()
    val state: LiveData<SearchUiState> = _state

    init {
        _state.postValue(SearchUiState.Success(emptyList()))
    }

    fun search(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchDiscogs(query)
        }
    }

    private suspend fun searchDiscogs(query: String) {
        _state.postValue(SearchUiState.Loading)
        discogsRepository.search(query).let {
            if (it.data != null) {
                _state.postValue(SearchUiState.Success(data = it.data))
            } else {
                _state.postValue(SearchUiState.Error(it.errorStringId!!))
            }
        }
    }
}
