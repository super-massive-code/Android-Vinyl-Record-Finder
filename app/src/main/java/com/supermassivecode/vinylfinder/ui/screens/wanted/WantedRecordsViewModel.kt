package com.supermassivecode.vinylfinder.ui.screens.wanted

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.data.local.WantedFoundRecordsRepository
import com.supermassivecode.vinylfinder.data.local.model.WantedRecordDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface WantedRecordsUiState {
    data class Error(@StringRes val alertStringId: Int) : WantedRecordsUiState
    data class Success(val data: List<WantedRecordDTO>) : WantedRecordsUiState
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
     */

    init {
        viewModelScope.launch(Dispatchers.IO) {
            //TODO what to do when we have no records? toast?
            _state.postValue(WantedRecordsUiState.Success(data = repository.getAllWantedRecordsAsDTO()))
        }
    }
}