package com.supermassivecode.vinylfinder.navigation

const val NAV_ARG_DISCOGS_REMOTE_ID = "discogsRemoteId"

sealed class NavigationScreen(val route: String) {
    object Search : NavigationScreen("search")
    object Detail : NavigationScreen("detail/{$NAV_ARG_DISCOGS_REMOTE_ID}") {
        fun createRoute(discogsRemoteId: Int) = "detail/$discogsRemoteId"
    }
}