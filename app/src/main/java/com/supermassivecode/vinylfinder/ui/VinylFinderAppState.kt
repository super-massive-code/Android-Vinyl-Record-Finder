package com.supermassivecode.vinylfinder.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberVinylFinderAppState(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current
) = remember(navController, context) {
    VinylFinderAppState(navController, context)
}

class VinylFinderAppState(
    val navController: NavHostController,
    val context: Context
)
