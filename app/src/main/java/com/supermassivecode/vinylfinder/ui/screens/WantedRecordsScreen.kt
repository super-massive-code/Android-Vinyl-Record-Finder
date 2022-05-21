package com.supermassivecode.vinylfinder.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.supermassivecode.vinylfinder.data.local.model.RecordInfoDTO
import org.koin.androidx.compose.getViewModel

@Composable
fun WantedRecordsScreen(
    navController: NavController,
    context: Context,
    viewModel: WantedRecordsViewModel = getViewModel()
) {
    val state by viewModel.state.observeAsState()
    when (val s = state) {
        is WantedRecordsUiState.Success -> {
            RecordList(records = s.data)
        }
        is WantedRecordsUiState.Error -> TODO()
        null -> {}
    }
}

@Composable
private fun RecordList(records: List<Map<RecordInfoDTO, Int>>) {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        items(items = records) { record ->
            RecordItem(record)
        }
    }
}

@Composable
private fun RecordItem(map: Map<RecordInfoDTO, Int>) {
    val record: RecordInfoDTO = map.keys.first()
    val foundCount = map.values.first()
    Card(
       modifier = Modifier
           .padding(8.dp)
    ) {
        Column {
            Text(record.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(record.year)
            Text(record.label)
            Text(record.catno)
        }
    }
}
