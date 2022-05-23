package com.supermassivecode.vinylfinder.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            Text(text = "Trigger Wanted Search")
        }
    }
}