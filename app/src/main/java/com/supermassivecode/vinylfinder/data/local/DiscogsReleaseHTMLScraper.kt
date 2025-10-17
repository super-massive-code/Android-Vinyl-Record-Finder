package com.supermassivecode.vinylfinder.data.local

import com.supermassivecode.vinylfinder.data.CurrencyUtils
import com.supermassivecode.vinylfinder.data.local.model.FoundRecordDTO
import com.supermassivecode.vinylfinder.data.local.model.Shop
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class DiscogsReleaseHTMLScraper(
    private val currencyUtils: CurrencyUtils
) {
    fun scrapeRelease(
        maxPrice: Float,
        localCurrencySymbol: String,
        htmlDocument: Document,
        originUrl: String,
    ): List<FoundRecordDTO> {
        return htmlDocument
            .select("table.table_block tr:not(.unavailable):not(:first-child)")
            .mapNotNull { row ->
                parseRow(row, maxPrice, localCurrencySymbol, originUrl)
            }
    }

    private fun parseRow(
        row: Element,
        maxPrice: Float,
        localCurrencySymbol: String,
        originUrl: String
    ): FoundRecordDTO? {
        val prices = extractPricesFromRow(row) ?: return null
        if (prices.totalPrice > maxPrice) return null
//        if (prices.currency != localCurrencySymbol) return null // TODO: use currency GBP or USD rather than $

        val sellerName = extractSellerName(row) ?: "Unknown"

        return FoundRecordDTO(
            shop = Shop.DISCOGS,
            url = originUrl,
            notes = "Seller name: $sellerName",
            recordPrice = prices.recordPrice,
            totalPriceIncShipping = prices.totalPrice,
            currency = prices.currency
        )
    }

    private data class PriceInfo(
        val recordPrice: Float,
        val totalPrice: Float,
        val currency: String
    )

    private fun extractPricesFromRow(row: Element): PriceInfo? {
        val priceCell = row.selectFirst("td.item_price") ?: return null

        // Get item price using data attributes
        val priceElement = priceCell.selectFirst("span.price") ?: return null
        val recordPrice = priceElement.attr("data-pricevalue").toFloatOrNull() ?: return null
        val currency = priceElement.attr("data-currency")

        // Get total price
        val totalPriceText = priceCell.selectFirst("span.converted_price")
            ?.ownText() // Gets direct text, ignoring children
            ?.trim()
            ?: return null

        val totalPrice = currencyUtils.stripNonNumericChars(totalPriceText)

        return PriceInfo(recordPrice, totalPrice, currency)
    }

    private fun extractSellerName(row: Element): String? {
        return row.selectFirst(".seller_block strong a")?.text()
    }
}