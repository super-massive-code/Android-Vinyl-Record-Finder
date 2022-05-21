package com.supermassivecode.vinylfinder.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.supermassivecode.vinylfinder.navigation.NAV_ARG_RECORD_INFO_JSON
import com.supermassivecode.vinylfinder.navigation.NavigationScreen
import com.supermassivecode.vinylfinder.ui.screens.RecordDetailScreen
import com.supermassivecode.vinylfinder.ui.screens.SearchScreen
import com.supermassivecode.vinylfinder.ui.screens.WantedRecordsScreen

@Composable
fun VinylFinderUI(
    appState: VinylFinderAppState = rememberVinylFinderAppState()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        NavHost(
            appState.navController,
            startDestination = NavigationScreen.Wanted.route
        ) {
            composable(
                route = NavigationScreen.Search.route
            ) {
                SearchScreen(appState.navController, appState.context)
            }
            composable(
                route = NavigationScreen.Detail.route,
                arguments = listOf(navArgument(NAV_ARG_RECORD_INFO_JSON) {
                    type = NavType.StringType
                    nullable = false
                })
            ) {
                Log.e("SMC", "BackStackTriggered")
                RecordDetailScreen(
                    it.arguments!!.getString(NAV_ARG_RECORD_INFO_JSON)!!,
                    appState.context
                )
            }
            composable(
                route = NavigationScreen.Wanted.route
            ) {
                WantedRecordsScreen(appState.navController, appState.context)
            }
        }
    }
}
