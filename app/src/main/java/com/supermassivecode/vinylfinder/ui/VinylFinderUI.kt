package com.supermassivecode.vinylfinder.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
        color = MaterialTheme.colorScheme.background
    ) {
        var topBarTitle by remember { mutableStateOf("") }

        Scaffold(
            topBar = { TopBar(topBarTitle) },
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    ScreenController(appState = appState) { titleText ->
                        topBarTitle = titleText
                    }
                }
            },
            bottomBar = { BottomBar(navHostController = appState.navController) }
        )
    }
}

@Composable
private fun ScreenController(appState: VinylFinderAppState, setTopBarText: (String) -> Unit) {
    NavHost(
        appState.navController,
        startDestination = NavigationScreen.Search.route
    ) {
        composable(
            route = NavigationScreen.Search.route
        ) {
            setTopBarText("Record Search")
            SearchScreen(appState.navController, appState.context)
        }
        composable(
            route = NavigationScreen.Detail.route,
            arguments = listOf(navArgument(NAV_ARG_RECORD_INFO_JSON) {
                type = NavType.StringType
                nullable = false
            })
        ) {
            setTopBarText("Record Detail")
            RecordDetailScreen(
                it.arguments!!.getString(NAV_ARG_RECORD_INFO_JSON)!!,
                appState.context
            )
        }
        composable(
            route = NavigationScreen.Wanted.route
        ) {
            setTopBarText("Wants List")
            WantedRecordsScreen(appState.navController)
        }
        composable(
            route = NavigationScreen.DeveloperOptions.route
        ) {
            setTopBarText("Developer Options")
            DeveloperOptionsScreen()
        }
        composable(
            route = NavigationScreen.Found.route,
            arguments = listOf(navArgument(NAV_ARG_RECORD_UID) {
                type = NavType.StringType
                nullable = false
            })
        ) {
            setTopBarText("Found Records")
            FoundSellersScreen(
                it.arguments!!.getString(NAV_ARG_RECORD_UID)!!
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(topBarText: String) {
    TopAppBar(
        title = {
            Text(
                text = topBarText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
private fun BottomBar(navHostController: NavHostController) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") },
            selected = currentRoute == NavigationScreen.Search.route,
            onClick = {
                navHostController.navigate(NavigationScreen.Search.route) {
                    restoreState = true
                    //TODO need to save state, where?
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Wants List") },
            label = { Text("Wants") },
            selected = currentRoute == NavigationScreen.Wanted.route,
            onClick = { navHostController.navigate(NavigationScreen.Wanted.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBox, contentDescription = "Developer Options") },
            label = { Text("Dev") },
            selected = currentRoute == NavigationScreen.DeveloperOptions.route,
            onClick = { navHostController.navigate(NavigationScreen.DeveloperOptions.route) }
        )
    }
}