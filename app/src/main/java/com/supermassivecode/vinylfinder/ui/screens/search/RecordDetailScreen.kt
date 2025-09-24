package com.supermassivecode.vinylfinder.ui.screens.search

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supermassivecode.vinylfinder.data.local.model.RecordInfoDTO
import com.supermassivecode.vinylfinder.data.local.model.RecordTrackDTO
import com.supermassivecode.vinylfinder.ui.GenericAlertDialog
import com.supermassivecode.vinylfinder.ui.theme.standardPadding
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecordDetailScreen(
    recordJson: String,
    context: Context,
    viewModel: RecordDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getReleaseDetail(RecordInfoDTO.fromJson(recordJson)!!)
    }
    Box(
        Modifier.fillMaxSize()
    ) {
        when (val s = state) {
            is DetailUiState.Loading ->
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            is DetailUiState.Success -> {
                Column(Modifier.fillMaxSize()) {
                    Header(s.data, s.inWatchList) { viewModel.toggleRecordInWatchList(s.data) }
                    s.data.tracks?.let { Tracks(it) }
                }
            }
            is DetailUiState.RequestMaxPriceForRecord -> {
                RequestMaxPrice(
                    s.title,
                    s.message,
                    onSave = { viewModel.addWantedRecord(s.record, it) },
                    onDecideLater = { viewModel.addWantedRecord(s.record) })
            }
            is DetailUiState.Error -> GenericAlertDialog(context, s.alertStringId)
        }
    }
}

@Composable
private fun Header(recordInfoDTO: RecordInfoDTO, inWatchList: Boolean, onClick: () -> Unit) {
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

            if (inWatchList) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Remove from watchlist",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .size(50.dp)
                )
            } else {
                Icon(
                    Icons.Filled.AddCircle,
                    contentDescription = "Add to watchlist",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(50.dp)
                )
            }
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
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = track.title,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun RequestMaxPrice(
    title: String,
    message: String,
    onSave: (Float) -> Unit,
    onDecideLater: () -> Unit
) {
    var priceText by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDecideLater,
        title = {
            Text(title)
        },
        text = {
            Column {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { newValue: String ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                            priceText = newValue  // Fix: actually assign the value
                        }
                    },
                    label = { Text("Max Price")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    isError = isError,
                    supportingText = if (isError) {
                        { Text("Please enter a valid price") }
                    } else null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val price = priceText.toFloatOrNull()
                    if (price != null && price > 0) {
                        onSave(price)
                    } else {
                        isError = true
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDecideLater) {
                Text("Decide Later")
            }
        }
    )
}