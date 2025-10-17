package com.supermassivecode.vinylfinder.data

import java.util.*


class CurrencyUtils(
    private val currency: Currency = Currency.getInstance(Locale.getDefault())
) {
    fun localSymbol(): String = currency.symbol

    fun localCurrencyCode(): String = currency.currencyCode

    companion object {

        fun stripNonNumericChars(string: String): Float {
            val re = Regex("[^0-9.]")
            val cleaned = re.replace(string, "")
            return cleaned.toFloat()
        }

        fun symbolForCode(currencyCode: String): String {
            return Currency.getInstance(currencyCode).symbol
        }
    }
}
