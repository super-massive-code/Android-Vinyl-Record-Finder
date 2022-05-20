package com.supermassivecode.vinylfinder.data.local

import com.supermassivecode.vinylfinder.data.local.model.FoundRecordInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements


class DiscogsWantedRecordWorker(
    private val wantedFoundRecordsRepository: WantedFoundRecordsRepository,
    private val ioDispatcher: CoroutineDispatcher
) : WantedRecordWorker {

    /**
     * Search Discogs MarketPlace
     * Pass in 1 record or multiple OR passing Wanted Records repository?
     * If match found, write back to Wanted Records repository with URL
     * trigger send of local message or email (should this just watch repository for results?)
     */


    override suspend fun doWork() {
        //TODO add delay so not hammering website or do this externally from call site?
        //TODO get max price from UI / DB
        //TODO get local currency symbol from?

        wantedFoundRecordsRepository.getAllWantedRecords().map { wantedRecord ->
            scrapeRelease(
                id = wantedRecord.discogsRemoteId,
                maxPrice = 100F,
                localCurrencySymbol = "$"
            ).map { foundRecord ->
                wantedFoundRecordsRepository.addFoundRecord(
                    wantedRecord.uid,
                    foundRecord.url,
                    notes = foundRecord.notes
                )
            }
        }
    }

    private suspend fun scrapeRelease(
        id: Int,
        maxPrice: Float,
        localCurrencySymbol: String
    ): List<FoundRecordInfo> = withContext(ioDispatcher) {

        val found = mutableListOf<FoundRecordInfo>()

        val url = "https://www.discogs.com/sell/release/$id?sort=price%2Casc&limit=250&page=1"

        val doc: Document = Jsoup.connect(url).get()
        val table: Element = doc.getElementsByClass("table_block")[0]
        val rows: Elements = table.getElementsByTag("tr")
        val available = Elements()
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
                    FoundRecordInfo(
                        url = url,
                        notes = "Seller name: $sellerName"
                    )
                )
            }
        }

        return@withContext found
    }
}
