package com.supermassivecode.vinylfinder.ui.screens.search

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supermassivecode.vinylfinder.data.local.model.RecordInfoDTO
import com.supermassivecode.vinylfinder.data.local.model.RecordTrackDTO
import com.supermassivecode.vinylfinder.ui.GenericAlertDialog
import com.supermassivecode.vinylfinder.ui.theme.standardPadding
import org.koin.androidx.compose.getViewModel

@Composable
fun RecordDetailScreen(
    recordJson: String,
    context: Context,
    viewModel: RecordDetailViewModel = getViewModel()
) {
    val state by viewModel.state.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getReleaseDetail(RecordInfoDTO.fromJson(recordJson)!!)
    }
    Box(
        Modifier.fillMaxSize()
    ) {
        when (val s = state) {
            is DetailUiState.Loading ->
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            is DetailUiState.Success -> {
                Column(Modifier.fillMaxSize()) {
                    Header(s.data) { viewModel.addRecordToWatchList(s.data) }
                    s.data.tracks?.let { Tracks(it) }
                }
            }
            is DetailUiState.Error -> GenericAlertDialog(context, s.alertStringId)
            null -> {}
        }
    }
}

@Composable
private fun Header(recordInfoDTO: RecordInfoDTO, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(
                start = standardPadding,
                end = standardPadding
            )
    ) {
        RecordItem(record = recordInfoDTO) {}
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(50.dp)
                .padding(end = standardPadding, bottom = standardPadding)
        ) {
            Icon(
                Icons.Filled.AddCircle,
                contentDescription = "add record",
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp)
            )
        }
    }
}

@Composable
private fun Tracks(tracks: List<RecordTrackDTO>) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(standardPadding),
        verticalArrangement = Arrangement.spacedBy(standardPadding)
    ) {
        items(tracks) { track ->
            TrackItem(track)
        }
    }
}

@Composable
private fun TrackItem(track: RecordTrackDTO) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(standardPadding),
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
