package com.supermassivecode.vinylfinder.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.supermassivecode.vinylfinder.data.local.model.RecordInfo
import org.koin.androidx.compose.getViewModel

@Composable
fun RecordDetailScreen(
    recordJson: String,
    viewModel: RecordDetailViewModel = getViewModel()
) {
    Log.e("SMC", "Detail loaded")
    //so it only runs once
    LaunchedEffect(Unit) {
        Log.e("SMC", "Detail loaded network call")
        viewModel.getReleaseDetail(RecordInfo.fromJson(recordJson)!!)
    }
    Box(
        Modifier.fillMaxSize()
    ) {

        Column(Modifier.fillMaxSize()) {
            // Look under the hood of observeAsState you'll see it using remember
            val record by viewModel.record.observeAsState()
            record?.let {
                Header(it)
                // Track list
                // Video's ?
            }
        }

        val isLoading by viewModel.isLoading.observeAsState()
        isLoading?.let {
            if (it) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun Header(recordInfo: RecordInfo) {
    AsyncImage(
        modifier = Modifier.size(400.dp),
        model = recordInfo.imageUrl,
        contentDescription = "record image"
    )
}
