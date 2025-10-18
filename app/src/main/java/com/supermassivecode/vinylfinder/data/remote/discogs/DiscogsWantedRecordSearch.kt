package com.supermassivecode.vinylfinder.data.remote.discogs

import com.supermassivecode.vinylfinder.data.CurrencyUtils
import com.supermassivecode.vinylfinder.data.local.DiscogsReleaseHTMLScraper
import com.supermassivecode.vinylfinder.data.local.model.FoundRecordDTO
import com.supermassivecode.vinylfinder.data.local.room.WantedRecord
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

data class DiscogsWantedResult(
    val found: List<Pair<WantedRecord, List<FoundRecordDTO>>>,
    val exception: Exception? = null
)

class DiscogsWantedSearch(
    private val currencyUtils: CurrencyUtils,
    private val scraper: DiscogsReleaseHTMLScraper
) {

    fun search(records: List<WantedRecord>): DiscogsWantedResult {
        val found = mutableListOf<Pair<WantedRecord, List<FoundRecordDTO>>>()

        for (wantedRecord in records) {
            try {
                val url = buildDiscogsUrl(wantedRecord.discogsRemoteId)
                val doc = fetchDocument(url)

                val scrapedRecords = scraper.scrapeRelease(
                    maxRecordPricePrice = wantedRecord.maxPrice!!,
                    localCurrencyCode = currencyUtils.localCurrencyCode(),
                    htmlDocument = doc,
                    originUrl = url
                )

                found.add(wantedRecord to scrapedRecords)
            } catch (ex: Exception) {
                return DiscogsWantedResult(found, ex)
            }
        }

        return DiscogsWantedResult(found)
    }

    private fun buildDiscogsUrl(releaseId: Int): String =
        "https://www.discogs.com/sell/release/$releaseId?sort=price%2Casc&limit=250&page=1"

    private fun fetchDocument(url: String): Document =
        Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
            .timeout(10000)
            .followRedirects(true)
            .get()
}
