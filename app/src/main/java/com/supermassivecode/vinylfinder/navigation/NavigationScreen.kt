package com.supermassivecode.vinylfinder.navigation

const val NAV_ARG_RECORD_INFO_JSON = "recordInfoJson"
const val NAV_ARG_RECORD_UID = "recordUid"

sealed class NavigationScreen(val route: String) {
    object Search : NavigationScreen("search")
    object Detail : NavigationScreen("detail/{$NAV_ARG_RECORD_INFO_JSON}") {
        fun createRoute(recordInfoJson: String) = "detail/$recordInfoJson"
    }
    object Wanted : NavigationScreen("wanted")
    object Found : NavigationScreen("found/{$NAV_ARG_RECORD_UID}") {
        fun createRoute(uid: String) = "found/$uid"
    }
    object DeveloperOptions : NavigationScreen("developerOptions")
}
