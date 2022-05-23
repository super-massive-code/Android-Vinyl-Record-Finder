package com.supermassivecode.vinylfinder.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import com.supermassivecode.vinylfinder.data.local.WantedRecordsWorkManager

class DeveloperOptionsViewModel(
    private val appContext: Context
) : ViewModel() {

    fun searchForWantedRecords() {
        WantedRecordsWorkManager.runOneTimeRequest(appContext)
        //TODO pass app context or?
    }
}
