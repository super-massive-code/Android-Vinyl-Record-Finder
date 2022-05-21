package com.supermassivecode.vinylfinder.ui.screens

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.data.local.WantedFoundRecordsRepository
import com.supermassivecode.vinylfinder.data.local.model.RecordInfoDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface WantedRecordsUiState {
    data class Error(@StringRes val alertStringId: Int) : WantedRecordsUiState
    data class Success(val data: List<Map<RecordInfoDTO, Int>>) : WantedRecordsUiState
}

class WantedRecordsViewModel(
    private val repository: WantedFoundRecordsRepository
) : ViewModel() {

    private var _state = MutableLiveData<WantedRecordsUiState>()
    val state: LiveData<WantedRecordsUiState> = _state

    /**
     * TODO:
     * viewOnDiscogs
     * delete
     * view found records + symbol indicating found number
     * test button to 'search now'
     */

    init {
        viewModelScope.launch(Dispatchers.IO) {
            //TODO what to do when we have no records? toast?
            _state.postValue(WantedRecordsUiState.Success(data = repository.getAllWantedRecordsDTO()))
        }
    }
}