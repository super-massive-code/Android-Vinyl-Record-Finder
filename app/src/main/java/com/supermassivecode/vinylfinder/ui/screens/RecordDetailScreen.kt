package com.supermassivecode.vinylfinder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supermassivecode.vinylfinder.data.local.model.RecordInfo
import com.supermassivecode.vinylfinder.data.local.model.RecordTrack
import org.koin.androidx.compose.getViewModel

@Composable
fun RecordDetailScreen(
    recordJson: String,
    viewModel: RecordDetailViewModel = getViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getReleaseDetail(RecordInfo.fromJson(recordJson)!!)
    }
    Box(
        Modifier.fillMaxSize()
    ) {
        Column(Modifier.fillMaxSize()) {
            val record by viewModel.record.observeAsState()
            record?.let { recordInfo ->
                Header(recordInfo)
                recordInfo.tracks?.let { Tracks(it) }
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
    Row() {
        RecordItem(record = recordInfo) {}

    }
}

@Composable
private fun Tracks(tracks: List<RecordTrack>) {
    LazyColumn {
        items(tracks) { track ->
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = track.position + ":",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = track.title)
                }
            }
        }
    }
}
