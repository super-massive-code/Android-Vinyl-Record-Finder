package com.supermassivecode.vinylfinder

import com.supermassivecode.vinylfinder.data.CurrencyUtils
import com.supermassivecode.vinylfinder.data.local.DiscogsReleaseHTMLScraper
import org.jsoup.Jsoup
import org.junit.Test
import kotlin.test.assertEquals


class DiscogsReleaseHTMLScraperTest {

    private val basePath = "../app/src/test/resources/html/discogs/"

    private fun loadFile(fileName: String) = TestResourceLoader.loadTextFile(basePath + fileName)

    @Test
    fun whenMaxPriceIs100AndCurrencyDollarShouldThen6ResultsShouldBeFound() {
        val html = loadFile("discogs-1596438-Bob-Dylan-The-Freewheelin-Bob-Dylan.html")
        val result = DiscogsReleaseHTMLScraper(CurrencyUtils()).scrapeRelease(
            maxPrice = 100.00f,
            localCurrencySymbol = "$",
            htmlDocument = Jsoup.parse(html),
            originUrl = ""
        )

        assertEquals(6, result.size)
    }

    @Test
    fun whenMaxPriceIs100AndCurrencyGBPShouldThen6ResultsShouldBeFound() {
        val html = loadFile("discogs-1596438-Bob-Dylan-The-Freewheelin-Bob-Dylan.html")
        val result = DiscogsReleaseHTMLScraper(CurrencyUtils()).scrapeRelease(
            maxPrice = 100f,
            localCurrencySymbol = "£",
            htmlDocument = Jsoup.parse(html),
            originUrl = ""
        )

        assertEquals(6, result.size)
    }
}
