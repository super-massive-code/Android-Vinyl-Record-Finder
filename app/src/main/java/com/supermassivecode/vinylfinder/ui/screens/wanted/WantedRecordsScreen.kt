package com.supermassivecode.vinylfinder.ui.screens.wanted

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.supermassivecode.vinylfinder.data.local.model.WantedRecordDTO
import com.supermassivecode.vinylfinder.navigation.NavigationScreen
import org.koin.androidx.compose.getViewModel

@Composable
fun WantedRecordsScreen(
    navController: NavController,
    viewModel: WantedRecordsViewModel = getViewModel()
) {
    val state by viewModel.state.observeAsState()
    when (val s = state) {
        is WantedRecordsUiState.Success -> {
            RecordList(records = s.data) { uid ->
                navController.navigate(NavigationScreen.Found.createRoute(uid))
            }
        }
        is WantedRecordsUiState.Error -> TODO()
        null -> {}
    }
}

@Composable
private fun RecordList(records: List<WantedRecordDTO>, showResults: (uid: String) -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        items(items = records) { dto ->
            RecordItem(dto) {
                showResults(it)
            }
        }
    }
}

@Composable
private fun RecordItem(dto: WantedRecordDTO, showFound: (uid: String) -> Unit) {
    //TODO: long click / swipe to delete?
    val record = dto.infoDTO
    val foundCount = dto.foundCount
    Card(
        Modifier
            .fillMaxWidth()
            .background(Color.LightGray),
    ) {
        Box(
            Modifier
                .background(Color.LightGray)
                .padding(8.dp)
                .clickable(enabled = foundCount > 0, onClick = { showFound(dto.databaseUid) })
        ) {
            Column()
            {
                Text(record.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(record.year)
                Text(record.label)
                Text(record.catno)
            }

            if (foundCount > 0) {
                Text(
                    foundCount.toString(),
                    modifier = Modifier
                        .background(Color.Red, shape = CircleShape)
                        .badgeLayout()
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }
}

fun Modifier.badgeLayout() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val minPadding = placeable.height / 4
        val width = maxOf(placeable.width + minPadding, placeable.height)
        layout(width, placeable.height) {
            placeable.place((width - placeable.width) / 2, 0)
        }
    }
