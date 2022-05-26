package com.supermassivecode.vinylfinder.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
        color = MaterialTheme.colors.primary
    ) {
        var topBarTitle by remember { mutableStateOf("") }

        Scaffold(
            topBar = { TopBar(topBarTitle) },
            content = { padding ->
                Box(modifier = Modifier.padding(bottom = padding.calculateBottomPadding())) {
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

@Composable
private fun TopBar(topBarText: String) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = Color.White,
        elevation = 10.dp
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = topBarText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
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
            icon = { Icon(Icons.Default.List, "Wants List Icon") },
            selected = currentRoute == NavigationScreen.Wanted.route,
            onClick = { navHostController.navigate(NavigationScreen.Wanted.route) }
        )
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
            icon = { Icon(Icons.Default.AccountBox, "Dev options Icon") },
            selected = currentRoute == NavigationScreen.DeveloperOptions.route,
            onClick = { navHostController.navigate(NavigationScreen.DeveloperOptions.route) }
        )
    }
}
