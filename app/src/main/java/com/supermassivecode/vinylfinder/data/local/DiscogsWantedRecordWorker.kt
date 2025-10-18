package com.supermassivecode.vinylfinder.data.local

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
            // TODO: log exception with partial results count: ${searchResults.found.size} records processed
        }
    }
}