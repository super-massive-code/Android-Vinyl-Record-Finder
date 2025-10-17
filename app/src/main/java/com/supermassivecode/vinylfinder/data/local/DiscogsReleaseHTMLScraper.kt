package com.supermassivecode.vinylfinder.data.local

import com.supermassivecode.vinylfinder.data.CurrencyUtils
import com.supermassivecode.vinylfinder.data.local.model.FoundRecordDTO
import com.supermassivecode.vinylfinder.data.local.model.Shop
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class DiscogsReleaseHTMLScraper {
    fun scrapeRelease(
        maxRecordPricePrice: Float,
        localCurrencyCode: String,
        htmlDocument: Document,
        originUrl: String,
    ): List<FoundRecordDTO> {
        return htmlDocument
            .select("table.table_block tr:not(.unavailable):not(:first-child)")
            .mapNotNull { row ->
                parseRow(row, maxRecordPricePrice, localCurrencyCode, originUrl)
            }
    }

    private fun parseRow(
        row: Element,
        maxRecordPricePrice: Float,
        localCurrencyCode: String,
        originUrl: String
    ): FoundRecordDTO? {
        try {
            val prices = extractPricesFromRow(row) ?: return null
            if (prices.currencyCode != localCurrencyCode) return null
            if (prices.recordPrice > maxRecordPricePrice) return null

            val sellerName = extractSellerName(row) ?: "Unknown"

            return FoundRecordDTO(
                shop = Shop.DISCOGS,
                url = originUrl,
                notes = "Seller name: $sellerName",
                recordPrice = prices.recordPrice,
                totalPriceIncShipping = prices.totalPrice,
                currencyCode = prices.currencyCode
            )
        } catch (_: Exception) {
            return null
        }
    }

    private data class PriceInfo(
        val recordPrice: Float,
        val totalPrice: Float,
        val currencyCode: String
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

        val totalPrice = CurrencyUtils.stripNonNumericChars(totalPriceText)

        return PriceInfo(recordPrice, totalPrice, currency)
    }

    private fun extractSellerName(row: Element): String? {
        return row.selectFirst(".seller_block strong a")?.text()
    }
}