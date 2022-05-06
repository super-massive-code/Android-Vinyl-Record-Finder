package com.supermassivecode.vinylfinder.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.supermassivecode.vinylfinder.data.local.model.RecordInfo
import org.koin.androidx.compose.getViewModel

@Composable
fun RecordDetailScreen(
    navController: NavController,
    recordJson: String,
    viewModel: RecordDetailViewModel = getViewModel()
) {
    viewModel.getReleaseDetail(RecordInfo.fromJson(recordJson)!!)

    Box(
        Modifier.fillMaxSize()
    ) {
        Column(Modifier.fillMaxSize()) {
            viewModel.record.observeAsState().value.run {
                //TODO: null check not idiomatic kotlin?
                if (this != null) {
                    Header(this)
                }
                // Tracklist
                // Video's ?
            }
        }

        viewModel.isLoading.observeAsState().value.run {
            if (this!!) {
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