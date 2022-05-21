package com.supermassivecode.vinylfinder.data

import java.util.*


class CurrencyUtils(
    private val currency: Currency = Currency.getInstance(Locale.getDefault())
) {
    fun localSymbol(): String = currency.symbol
}
