package com.supermassivecode.vinylfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.supermassivecode.vinylfinder.data.remote.DiscogsService
import com.supermassivecode.vinylfinder.data.remote.model.Result
import com.supermassivecode.vinylfinder.ui.theme.VinylFinderTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            searchDiscogs("Carl Taylor Static")
        }
    }

    private suspend fun searchDiscogs(query: String) {
        //TODO move this into viewModel etc
        val token: String = getString(R.string.discogs_token)
        val response = DiscogsService.getService().search(token = token, query = query)
        if (response.isSuccessful) {
            refreshView(response.body()!!.results)
            //TODO: replace with model for UI consumption
        }
    }

    private fun refreshView(records: List<Result>) {
        setContent {
            VinylFinderTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    MainRecordList(records)
                }
            }
        }
    }
}

