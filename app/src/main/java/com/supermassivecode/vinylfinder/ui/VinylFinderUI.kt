package com.supermassivecode.vinylfinder.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.supermassivecode.vinylfinder.navigation.NAV_ARG_RECORD_INFO_JSON
import com.supermassivecode.vinylfinder.navigation.NAV_ARG_RECORD_UID
import com.supermassivecode.vinylfinder.navigation.NavigationScreen
import com.supermassivecode.vinylfinder.ui.screens.developeroptions.DeveloperOptionsScreen
import com.supermassivecode.vinylfinder.ui.screens.search.RecordDetailScreen
import com.supermassivecode.vinylfinder.ui.screens.search.SearchScreen
import com.supermassivecode.vinylfinder.ui.screens.wanted.FoundSellersScreen
import com.supermassivecode.vinylfinder.ui.screens.wanted.WantedRecordsScreen

@Composable
fun VinylFinderUI(
    appState: VinylFinderAppState = rememberVinylFinderAppState()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            bottomBar = { BottomBar(navHostController = appState.navController) },
            content = { ScreenController(appState = appState) }
        )
    }
}

@Composable
private fun ScreenController(appState: VinylFinderAppState) {
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
            WantedRecordsScreen(appState.navController)
        }
        composable(
            route = NavigationScreen.DeveloperOptions.route
        ) {
            DeveloperOptionsScreen()
        }
        composable(
            route = NavigationScreen.Found.route,
            arguments = listOf(navArgument(NAV_ARG_RECORD_UID) {
                type = NavType.StringType
                nullable = false
            })
        ) {
            FoundSellersScreen(
                it.arguments!!.getString(NAV_ARG_RECORD_UID)!!
            )
        }
    }
}

@Composable
private fun BottomBar(navHostController: NavHostController) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    BottomNavigation(
        elevation = 12.dp
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Search, "Search Icon") },
            selected = currentRoute == NavigationScreen.Search.route,
            onClick = {
                navHostController.navigate(NavigationScreen.Search.route) {
                    restoreState = true
                    //TODO need to save state, where?
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.List, "Wants List Icon") },
            selected = currentRoute == NavigationScreen.Wanted.route,
            onClick = { navHostController.navigate(NavigationScreen.Wanted.route) }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.AccountBox, "Dev options Icon") },
            selected = currentRoute == NavigationScreen.DeveloperOptions.route,
            onClick = { navHostController.navigate(NavigationScreen.DeveloperOptions.route) }
        )
    }
}
