package com.supermassivecode.vinylfinder

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    fun toLocalString(
        amount: BigDecimal,
        locale: Locale = Locale.getDefault(),
    ): String = NumberFormat.getCurrencyInstance(locale).format(amount)

    fun fromLocalString(
        amountString: String,
        locale: Locale = Locale.getDefault(),
    ): BigDecimal? =
        try {
            val parsedNumber = NumberFormat.getCurrencyInstance(locale).parse(amountString)
            parsedNumber?.toString()?.let { BigDecimal(it) }
        } catch (e: Exception) {
            Logger.logException(e)
            null
        }
}
