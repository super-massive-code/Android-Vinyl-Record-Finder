package com.supermassivecode.vinylfinder.data.local

import com.supermassivecode.vinylfinder.data.CurrencyUtils
import com.supermassivecode.vinylfinder.data.local.model.FoundRecordDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements


class DiscogsWantedRecordWorker(
    private val wantedFoundRecordsRepository: WantedFoundRecordsRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val currencyUtils: CurrencyUtils,
) {

    /**
     * Search Discogs MarketPlace
     * trigger send of local message or email (should this just watch repository for results?)
     */

    suspend fun doWork() {
        //TODO add delay so not hammering website or do this externally from call site?
        //TODO get max price from UI / DB
        wantedFoundRecordsRepository.getAllWantedRecords().map { wantedRecord ->
            scrapeRelease(
                id = wantedRecord.discogsRemoteId,
                maxPrice = 100F,
                localCurrencySymbol = currencyUtils.localSymbol()
            ).map { foundRecord ->
                wantedFoundRecordsRepository.addFoundRecordIfNotExists(
                    wantedRecord.uid,
                    foundRecord,
                )
            }
        }
    }

    private suspend fun scrapeRelease(
        id: Int,
        maxPrice: Float,
        localCurrencySymbol: String
    ): List<FoundRecordDTO> = withContext(ioDispatcher) {

        val found = mutableListOf<FoundRecordDTO>()

        val url = "https://www.discogs.com/sell/release/$id?sort=price%2Casc&limit=250&page=1"

        val doc: Document = Jsoup.connect(url).get()
        val table: Element = doc.getElementsByClass("table_block")[0]
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
            val justPrice = java.lang.Float.valueOf(priceString.substring(1))
            if (justPrice <= maxPrice) {
                val sellerBlock: Element = row.getElementsByClass("seller_block")[0]
                val sellerName: String = java.lang.String.valueOf(
                    sellerBlock.allElements[2].allElements[0].childNodes()[0]
                        .childNodes()[0]
                )
                found.add(
                    FoundRecordDTO(
                        url = url,
                        seller = "$sellerName discogs",
                        price = justPrice,
                        currency = localCurrencySymbol
                    )
                )
            }
        }

        return@withContext found
    }
}
