package com.supermassivecode.vinylfinder

import com.supermassivecode.vinylfinder.data.CurrencyUtils
import com.supermassivecode.vinylfinder.data.local.DiscogsReleaseHTMLScraper
import org.jsoup.Jsoup
import org.junit.Test
import kotlin.test.assertEquals


class DiscogsReleaseHTMLScraperTest {

    private val basePath = "../app/src/test/resources/html/discogs/"

    private val bobDylanHTML = "discogs-1596438-Bob-Dylan-The-Freewheelin-Bob-Dylan.html"
    private val carlCarltonHTML = "discogs-942201-Carl-Carlton-Carl-Carlton.html"
    private val carlTaylorHTML = "discogs-231834-Carl-Taylor-Static.html"

    private fun loadFile(fileName: String) = TestResourceLoader.loadTextFile(basePath + fileName)

    @Test
    fun withBobDylanHTMLWhenMaxPriceIs42Then2ResultsShouldBeFound() {
        val result = DiscogsReleaseHTMLScraper(CurrencyUtils()).scrapeRelease(
            maxPriceIncludingShipping = 42.00f,
            localCurrencySymbol = "",
            htmlDocument = Jsoup.parse(loadFile(bobDylanHTML)),
            originUrl = ""
        )

        assertEquals(2, result.size)
    }

    @Test
    fun withBobDylanHTMLWhenMaxPriceIs50Then4ResultsShouldBeFound() {
        val result = DiscogsReleaseHTMLScraper(CurrencyUtils()).scrapeRelease(
            maxPriceIncludingShipping = 50.00f,
            localCurrencySymbol = "",
            htmlDocument = Jsoup.parse(loadFile(bobDylanHTML)),
            originUrl = ""
        )

        assertEquals(4, result.size)
    }

    @Test
    fun withCarlCarltonWhenMaxPriceIs10Then0ShouldBeFound() {
        val result = DiscogsReleaseHTMLScraper(CurrencyUtils()).scrapeRelease(
            maxPriceIncludingShipping = 10.00f,
            localCurrencySymbol = "",
            htmlDocument = Jsoup.parse(loadFile(carlCarltonHTML)),
            originUrl = ""
        )

        assertEquals(0, result.size)
    }

    @Test
    fun withCarlCarltonWhenMaxPriceIs30Then20ShouldBeFound() {
        val result = DiscogsReleaseHTMLScraper(CurrencyUtils()).scrapeRelease(
            maxPriceIncludingShipping = 30.00f,
            localCurrencySymbol = "",
            htmlDocument = Jsoup.parse(loadFile(carlCarltonHTML)),
            originUrl = ""
        )

        assertEquals(20, result.size)
    }

    @Test
    fun withCarlTaylorWhenMaxPriceIsxThenXShouldBeFound() {
        val result = DiscogsReleaseHTMLScraper(CurrencyUtils()).scrapeRelease(
            maxPriceIncludingShipping = 5.00f,
            localCurrencySymbol = "",
            htmlDocument = Jsoup.parse(loadFile(carlTaylorHTML)),
            originUrl = ""
        )

        assertEquals(2, result.size)
    }
}
