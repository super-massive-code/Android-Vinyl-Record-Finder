package com.supermassivecode.vinylfinder.ui.screens.wanted

import android.R
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.supermassivecode.vinylfinder.data.CurrencyUtils
import com.supermassivecode.vinylfinder.data.local.model.WantedRecordDTO
import com.supermassivecode.vinylfinder.navigation.NavigationScreen
import com.supermassivecode.vinylfinder.ui.theme.standardPadding
import org.koin.androidx.compose.koinViewModel

@Composable
fun WantedRecordsScreen(
    navController: NavController,
    viewModel: WantedRecordsViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadWantedRecords()
    }

    val state by viewModel.state.collectAsState()
    when (val s = state) {
        is WantedRecordsUiState.Success -> {
            RecordList(records = s.data) { uid ->
                navController.navigate(NavigationScreen.Found.createRoute(uid))
            }
        }
        is WantedRecordsUiState.Error -> {
            // TODO: Handle error state with proper error UI
            Text(
                text = "Error loading wanted records",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(standardPadding)
            )
        }
        WantedRecordsUiState.Loading -> {}
    }
}

@Composable
private fun RecordList(records: List<WantedRecordDTO>, showResults: (uid: String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(standardPadding),
        verticalArrangement = Arrangement.spacedBy(standardPadding)
    ) {
        items(items = records) { dto ->
            RecordItem(dto) {
                showResults(it)
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun RecordItem(dto: WantedRecordDTO, showFound: (uid: String) -> Unit) {
    //TODO: long click / swipe to delete?
    val record = dto.infoDTO
    val foundCount = dto.foundCount

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(standardPadding)
                .clickable(enabled = foundCount > 0, onClick = { showFound(dto.databaseUid) })
        ) {
            Column {
                Text(
                    text = record.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = record.year,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = record.label,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = record.catno,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (dto.maxPrice != null) {
                        "My max price: ${CurrencyUtils().localSymbol()}${String.format("%.2f", dto.maxPrice)}"
                    } else {
                        "My max price: Not set"
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (foundCount > 0) {
                Badge(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Text(
                        text = foundCount.toString(),
                        color = MaterialTheme.colorScheme.onError
                    )
                }
            }
        }
    }
}

// Alternative implementation using custom badge layout if preferred
@Composable
private fun RecordItemWithCustomBadge(dto: WantedRecordDTO, showFound: (uid: String) -> Unit) {
    val record = dto.infoDTO
    val foundCount = dto.foundCount

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(standardPadding)
                .clickable(enabled = foundCount > 0, onClick = { showFound(dto.databaseUid) })
        ) {
            Column {
                Text(
                    text = record.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = record.year,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = record.label,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = record.catno,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (foundCount > 0) {
                Text(
                    text = foundCount.toString(),
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.error,
                            shape = CircleShape
                        )
                        .badgeLayout()
                        .align(Alignment.BottomEnd),
                    color = MaterialTheme.colorScheme.onError
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