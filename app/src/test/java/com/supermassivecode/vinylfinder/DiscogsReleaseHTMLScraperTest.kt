import com.supermassivecode.vinylfinder.TestResourceLoader
import com.supermassivecode.vinylfinder.data.local.DiscogsReleaseHTMLScraper
import org.jsoup.Jsoup
import org.junit.Test
import kotlin.test.assertEquals

class DiscogsReleaseHTMLScraperTest {

    /**
     * Scraper Ignores unavailable items in HTML (not shipped to users country) regardless
     * of Currency Code set
     */

    private val scraper = DiscogsReleaseHTMLScraper()

    companion object {
        private const val basePath = "html/discogs/"
        private const val bobDylanHTML = "discogs-1596438-Bob-Dylan-The-Freewheelin-Bob-Dylan.html"
        private const val carlCarltonHTML = "discogs-942201-Carl-Carlton-Carl-Carlton.html"
        private const val carlTaylorHTML = "discogs-231834-Carl-Taylor-Static.html"
    }

    private fun testScraper(
        fileName: String,
        maxPrice: Float,
        expectedResults: Int,
        currencyCode: String) {
        val result = scraper.scrapeRelease(
            maxRecordPricePrice = maxPrice,
            localCurrencyCode = currencyCode,
            htmlDocument = Jsoup.parse(TestResourceLoader.loadTextFile(basePath + fileName)),
            originUrl = ""
        )

        assertEquals(expectedResults, result.size)
    }

    @Test
    fun testBobDylanWithMaxPriceToCaptureAll() {
        testScraper(bobDylanHTML, 100000.00f, 4, currencyCode = "USD")
    }

    @Test
    fun testCarlCarltonWithMaxPrice1() {
        testScraper(carlCarltonHTML, 1.00f, 0, currencyCode = "USD")
    }

    @Test
    fun testCarlCarltonWithMaxPrice100() {
        testScraper(carlCarltonHTML, 100.00f, 15, currencyCode = "USD")
    }

    @Test
    fun testCarlTaylorWithMaxPrice5() {
        testScraper(carlTaylorHTML, 5.00f, 5, currencyCode = "GBP")
    }
}