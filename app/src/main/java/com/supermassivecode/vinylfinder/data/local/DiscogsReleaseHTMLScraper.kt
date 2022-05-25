package com.supermassivecode.vinylfinder.data.local

import com.supermassivecode.vinylfinder.data.CurrencyUtils
import com.supermassivecode.vinylfinder.data.local.model.FoundRecordDTO
import com.supermassivecode.vinylfinder.data.local.model.Shop
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements

class DiscogsReleaseHTMLScraper(
    private val currencyUtils: CurrencyUtils
) {

    fun scrapeRelease(
        maxPrice: Float,
        localCurrencySymbol: String,
        htmlDocument: Document,
        originUrl: String,
    ): List<FoundRecordDTO> {

        val found = mutableListOf<FoundRecordDTO>()

        val table: Element = htmlDocument.getElementsByClass("table_block")[0]
        val rows: Elements = table.getElementsByTag("tr")
        val available = Elements()
        // Remove table header
        rows.removeAt(0)

        for (row in rows) {
            if (!row.hasClass("unavailable")) {
                available.add(row)
            }
        }

        for (row in available) {
            val priceSection: Element = row.getElementsByClass("item_price")[0]
            var localPrice: Element = priceSection.getElementsByClass("price")[0]
            var priceString: String
            if (localPrice.getElementsContainingText(localCurrencySymbol).size == 0) {
                localPrice = priceSection.getElementsByClass("converted_price")[0]
                priceString = (localPrice.childNodes()[1] as TextNode).wholeText
            } else {
                priceString = (localPrice.childNodes()[0] as TextNode).wholeText
            }
            val justPrice = currencyUtils.convertFromString(priceString)
            if (justPrice <= maxPrice) {
                val sellerBlock: Element = row.getElementsByClass("seller_block")[0]
                val sellerName: String = java.lang.String.valueOf(
                    sellerBlock.allElements[2].allElements[0].childNodes()[0]
                        .childNodes()[0]
                )
                found.add(
                    FoundRecordDTO(
                        shop = Shop.DISCOGS,
                        url = originUrl,
                        notes = "Seller name: $sellerName",
                        price = justPrice,
                        currency = localCurrencySymbol
                    )
                )
            }
        }

        return found
    }
}
