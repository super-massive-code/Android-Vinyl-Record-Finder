import com.supermassivecode.vinylfinder.TestResourceLoader
import com.supermassivecode.vinylfinder.data.CurrencyUtils
import com.supermassivecode.vinylfinder.data.local.DiscogsReleaseHTMLScraper
import org.jsoup.Jsoup
import org.junit.Test
import kotlin.test.assertEquals

class DiscogsReleaseHTMLScraperTest {

    /**
     * Scraper Ignores unavailable items in HTML (not shipped to users country)
     */

    private val scraper = DiscogsReleaseHTMLScraper(CurrencyUtils())

    companion object {
        private const val basePath = "html/discogs/"
        private const val bobDylanHTML = "discogs-1596438-Bob-Dylan-The-Freewheelin-Bob-Dylan.html"
        private const val carlCarltonHTML = "discogs-942201-Carl-Carlton-Carl-Carlton.html"
        private const val carlTaylorHTML = "discogs-231834-Carl-Taylor-Static.html"
    }

    private fun testScraper(fileName: String, maxPrice: Float, expectedResults: Int) {
        val result = scraper.scrapeRelease(
            maxRecordPricePrice = maxPrice,
            localCurrencySymbol = "",
            htmlDocument = Jsoup.parse(TestResourceLoader.loadTextFile(basePath + fileName)),
            originUrl = ""
        )

        assertEquals(expectedResults, result.size)
    }

    @Test
    fun testBobDylanWithMaxPriceToCaptureAll() {
        testScraper(bobDylanHTML, 100000.00f, 6)
    }

    @Test
    fun testCarlCarltonWithMaxPrice1() {
        testScraper(carlCarltonHTML, 1.00f, 0)
    }

    @Test
    fun testCarlCarltonWithMaxPrice100() {
        testScraper(carlCarltonHTML, 100.00f, 26)
    }

    @Test
    fun testCarlTaylorWithMaxPrice5() {
        testScraper(carlTaylorHTML, 5.00f, 11)
    }
}