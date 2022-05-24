package com.supermassivecode.vinylfinder.data.local.model

import androidx.annotation.DrawableRes
import com.supermassivecode.vinylfinder.R

enum class Shop(
    val shopName: String,
    @DrawableRes val imageId: Int
) {
    DISCOGS("Discogs", R.drawable.discogs_icon),
    EBAY("eBay", R.drawable.ebay_icon)
}
