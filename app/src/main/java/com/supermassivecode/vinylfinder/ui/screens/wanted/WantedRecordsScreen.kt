package com.supermassivecode.vinylfinder.ui.screens.wanted

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
            RecordList(
                records = s.data,
                showResults = { discogsRemoteId ->
                    navController.navigate(NavigationScreen.Found.createRoute(discogsRemoteId))
                },
                onDelete = { discogsRemoteId ->
                    viewModel.deleteWantedRecord(discogsRemoteId)
                }
            )
        }
        is WantedRecordsUiState.Error -> {
            Text(
                text = "Error loading wanted records",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(standardPadding)
            )
        }
        WantedRecordsUiState.Loading -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordList(
    records: List<WantedRecordDTO>,
    showResults: (databaseUid: String) -> Unit,
    onDelete: (discogsRemoteId: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(standardPadding),
        verticalArrangement = Arrangement.spacedBy(standardPadding)
    ) {
        items(
            items = records,
            key = { it.databaseUid }
        ) { dto ->
            SwipeToDeleteItem(
                dto = dto,
                onDelete = { onDelete(dto.infoDTO.discogsRemoteId) },
                showFound = { showResults(it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDeleteItem(
    dto: WantedRecordDTO,
    onDelete: () -> Unit,
    showFound: (uid: String) -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                    else -> Color.Transparent
                },
                label = "background_color"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true
    ) {
        RecordItem(dto, showFound)
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun RecordItem(dto: WantedRecordDTO, showFound: (uid: String) -> Unit) {
    val record = dto.infoDTO
    val foundCount = dto.foundCount

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(standardPadding)
                .clickable(enabled = foundCount > 0, onClick = { showFound(dto.databaseUid) })
                .fillMaxWidth()
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
                        .align(Alignment.BottomEnd)
                        .scale(1.3f),
                    containerColor = Color.White
                ) {
                    Text(
                        text = foundCount.toString(),
                        color = Color.DarkGray,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
