package com.supermassivecode.vinylfinder.data.local

import com.supermassivecode.vinylfinder.data.CurrencyUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class DiscogsWantedRecordWorker(
    private val wantedFoundRecordsRepository: WantedFoundRecordsRepository,
    private val currencyUtils: CurrencyUtils,
    private val scraper: DiscogsReleaseHTMLScraper
) {

    /**
     * Search Discogs MarketPlace
     * trigger send of local message or email (should this just watch repository for results?)
     */

    suspend fun doWork() {
        //TODO add delay so not hammering website or do this externally from call site?
        //TODO get max price from UI / DB
        wantedFoundRecordsRepository.getAllWantedRecords().map { wantedRecord ->

            val url = "https://www.discogs.com/sell/release/${wantedRecord.discogsRemoteId}?sort=price%2Casc&limit=250&page=1"
            val doc: Document = Jsoup.connect(url).get()

            scraper.scrapeRelease(
                maxPriceIncludingShipping = 100F,
                localCurrencySymbol = currencyUtils.localSymbol(),
                htmlDocument = doc,
                originUrl = url
            ).map { foundRecord ->
                wantedFoundRecordsRepository.addFoundRecordIfNotExists(
                    wantedRecord.uid,
                    foundRecord,
                )
            }
        }
    }
}
