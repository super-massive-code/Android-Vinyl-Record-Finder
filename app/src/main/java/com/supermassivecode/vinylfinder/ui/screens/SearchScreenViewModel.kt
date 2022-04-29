package com.supermassivecode.vinylfinder.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.vinylfinder.R
import com.supermassivecode.vinylfinder.data.remote.DiscogsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.supermassivecode.vinylfinder.data.remote.model.Result

class SearchScreenViewModel: ViewModel() {

    private var _records = MutableLiveData<List<Result>>()
    val records: LiveData<List<Result>> = _records

    init {
        search("Carl Taylor Static")
    }

    fun search(query: String) {
        viewModelScope.launch {
            searchDiscogs(query)
        }
    }

    private suspend fun searchDiscogs(query: String) {
        val token: String = getString(R.string.discogs_token)
        val response = DiscogsService.getService().search(token, query = query)
        if (response.isSuccessful) {
            _records.value = response.body()?.results
            //TODO: replace with model for UI consumption
        }
    }
}