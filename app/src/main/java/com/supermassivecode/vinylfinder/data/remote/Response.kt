package com.supermassivecode.vinylfinder.data.remote

import com.supermassivecode.vinylfinder.R

data class Response<T>(
    val data: T? = null,
    val errorStringId: Int? = null
) {
    companion object {
        fun parseServerErrorCode(code: Int): Int {
            return when (code) {
                400 -> R.string.server_error_400
                401 -> R.string.server_error_401
                403 -> R.string.server_error_403
                404 -> R.string.server_error_404
                500 -> R.string.server_error_500
                else -> R.string.server_error_generic
            }
        }
    }
}
