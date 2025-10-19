package com.supermassivecode.vinylfinder.data.local

import com.supermassivecode.vinylfinder.Logger
import com.supermassivecode.vinylfinder.data.remote.discogs.DiscogsWantedSearch


class DiscogsWantedRecordWorker(
    private val wantedFoundRecordsRepository: WantedFoundRecordsRepository,
    private val discogsWantedRecordSearch: DiscogsWantedSearch
) {
    /**
     * Search Discogs MarketPlace
     * TODO: trigger send of local message or email (should this just watch repository for results?)
     */
    suspend fun doWork() {
        val searchResults = discogsWantedRecordSearch.search(wantedFoundRecordsRepository.getAllWithMaxPriceSet())
        searchResults.found.forEach { (wantedRecord, foundRecords) ->
            foundRecords.forEach { found ->
                wantedFoundRecordsRepository.addFoundRecordIfNotExists(
                    wantedRecord.uid,
                    found,
                )
            }
        }

        searchResults.exception?.let { exception ->
            Logger.logException(exception, "Found record count ${searchResults.found.size}")
        }
    }
}