package com.supermassivecode.vinylfinder.navigation

const val NAV_ARG_RECORD_INFO_JSON = "recordInfoJson"

sealed class NavigationScreen(val route: String) {
    object Search : NavigationScreen("search")
    object Detail : NavigationScreen("detail/{$NAV_ARG_RECORD_INFO_JSON}") {
        fun createRoute(recordInfoJson: String) = "detail/$recordInfoJson"
    }
    object Wanted : NavigationScreen("wanted")
}