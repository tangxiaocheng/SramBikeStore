package app.sram.bikestore.di

import android.app.Application
import okhttp3.HttpUrl

/**
 * Wraps all level configuration such server url & application context.
 */
class AppConfig(
    val serverUrl: HttpUrl,
    val application: Application
)
