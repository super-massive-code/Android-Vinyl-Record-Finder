package com.supermassivecode.vinylfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.supermassivecode.vinylfinder.navigation.NavigationScreen
import com.supermassivecode.vinylfinder.ui.screens.SearchScreen
import com.supermassivecode.vinylfinder.ui.screens.SearchScreenViewModel
import com.supermassivecode.vinylfinder.ui.theme.VinylFinderTheme

class MainActivity : ComponentActivity() {

    private val searchScreenViewModel: SearchScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VinylFinderTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = NavigationScreen.SEARCH_SCREEN.name) {
                        composable(NavigationScreen.SEARCH_SCREEN.name) {
                            SearchScreen(navController, searchScreenViewModel)
                        }
                    }
                }
            }
        }
    }
}

