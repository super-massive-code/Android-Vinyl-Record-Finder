package com.supermassivecode.vinylfinder.ui.screens

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supermassivecode.vinylfinder.data.local.model.RecordInfo
import com.supermassivecode.vinylfinder.data.local.model.RecordTrack
import com.supermassivecode.vinylfinder.ui.GenericAlertDialog
import org.koin.androidx.compose.getViewModel

@Composable
fun RecordDetailScreen(
    recordJson: String,
    context: Context,
    viewModel: RecordDetailViewModel = getViewModel()
) {
    val state by viewModel.state.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getReleaseDetail(RecordInfo.fromJson(recordJson)!!)
    }
    Box(
        Modifier.fillMaxSize()
    ) {
        Column(Modifier.fillMaxSize()) {

            when (val s = state) {
                is DetailUiState.Loading ->
                    CircularProgressIndicator(
                        //TODO Centre not working as its in a column
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                is DetailUiState.Success -> {
                    Header(s.data) { viewModel.addRecordToWatchList(s.data) }
                    s.data.tracks?.let { Tracks(it) }
                }
                is DetailUiState.Error -> GenericAlertDialog(context, s.alertStringId)
                null -> {}
            }
        }
    }
}

@Composable
private fun Header(recordInfo: RecordInfo, onClick: () -> Unit) {
    Box {
        RecordItem(record = recordInfo) {}
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(50.dp)
        ) {
            Icon(
                Icons.Filled.AddCircle,
                contentDescription = "add record",
                tint = Color.Black,
                modifier = Modifier
                    .size(50.dp)
            )
        }
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
