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
     * TODO: trigger send of local message or email (should this just watch repository for results?)
     */

    suspend fun doWork() {
        //TODO add delay so not hammering website or do this externally from call site?
        wantedFoundRecordsRepository.getAllWantedRecords().map { wantedRecord ->
            if (wantedRecord.maxPrice ==  null) { return }

            val url = "https://www.discogs.com/sell/release/${wantedRecord.discogsRemoteId}?sort=price%2Casc&limit=250&page=1"
            val doc: Document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(10000) // 10 seconds
                .followRedirects(true)
                .get()
            scraper.scrapeRelease(
                maxPriceIncludingShipping = wantedRecord.maxPrice,
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
