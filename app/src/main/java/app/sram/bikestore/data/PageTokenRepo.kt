package app.sram.bikestore.data

import app.sram.bikestore.di.AppScope
import javax.inject.Inject

/**
 * Return a [PageStatus] based on the passed [LoadType] & the cached page token in shared preference.
 */
@AppScope
open class PageTokenRepo @Inject constructor() {
    private var nextPageToken: String? = null

    fun get(): String? {
        return nextPageToken
    }

    fun put(newToken: String?): Boolean {
        nextPageToken = newToken
        return newToken != null
    }
}
