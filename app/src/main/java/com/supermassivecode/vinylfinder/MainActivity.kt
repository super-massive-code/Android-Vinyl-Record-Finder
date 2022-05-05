package com.supermassivecode.vinylfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.supermassivecode.vinylfinder.navigation.NAV_ARG_RECORD_INFO_JSON
import com.supermassivecode.vinylfinder.navigation.NavigationScreen
import com.supermassivecode.vinylfinder.ui.screens.RecordDetailScreen
import com.supermassivecode.vinylfinder.ui.screens.RecordDetailViewModel
import com.supermassivecode.vinylfinder.ui.screens.SearchScreen
import com.supermassivecode.vinylfinder.ui.screens.SearchScreenViewModel
import com.supermassivecode.vinylfinder.ui.theme.VinylFinderTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val searchScreenViewModel: SearchScreenViewModel by inject()
    private val recordDetailViewModel: RecordDetailViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VinylFinderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController,
                        startDestination = NavigationScreen.Search.route
                    ) {
                        composable(
                            route = NavigationScreen.Search.route
                        ) {
                            SearchScreen(navController, searchScreenViewModel)
                        }
                        composable(
                            route = NavigationScreen.Detail.route,
                            arguments = listOf(navArgument(NAV_ARG_RECORD_INFO_JSON) {
                                type = NavType.StringType
                                nullable = false
                            })
                        ) {
                            RecordDetailScreen(
                                navController,
                                recordDetailViewModel,
                                it.arguments!!.getString(NAV_ARG_RECORD_INFO_JSON)!!
                            )
                        }
                    }
                }
            }
        }
    }
}
