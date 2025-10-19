import com.supermassivecode.vinylfinder.data.local.KeyValueStore
import java.text.SimpleDateFormat
import java.util.*

class TimestampManager(private val keyValueStore: KeyValueStore) {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun stampRecordPriceCheck() = keyValueStore.store(
        KeyValueStore.LAST_RECORD_PRICE_CHECK,
        System.currentTimeMillis()
    )

    fun readLastRecordPriceCheck(): Long? = keyValueStore.read<Long?>(KeyValueStore.LAST_RECORD_PRICE_CHECK)
    fun getLastPriceCheckFormatted(): String? =
        readLastRecordPriceCheck()?.let { epochToLocalDateTimeString(it) }
    fun epochToLocalDateTimeString(epochMillis: Long): String = formatter.format(Date(epochMillis))
}