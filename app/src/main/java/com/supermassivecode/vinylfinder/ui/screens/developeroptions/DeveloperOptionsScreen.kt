package com.supermassivecode.vinylfinder.ui.screens.developeroptions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel

@Composable
fun DeveloperOptionsScreen(
    viewModel: DeveloperOptionsViewModel = getViewModel()
) {
    Column(
        Modifier.padding(8.dp)
    ) {
        Button(onClick = { viewModel.searchForWantedRecords() }) {
            Text(text = "Trigger Wanted Search", color = Color.White)
        }
    }
}