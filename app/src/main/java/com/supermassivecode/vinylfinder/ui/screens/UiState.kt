package com.supermassivecode.vinylfinder.ui.screens

data class UiState<T>(
    val isLoading: Boolean = false,
    val alertStringId: Int? = null,
    val data: T? = null
)
