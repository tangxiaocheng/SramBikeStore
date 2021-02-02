package app.sram.bikestore

import app.sram.bikestore.di.AppConfig
import app.sram.bikestore.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import okhttp3.HttpUrl.Companion.toHttpUrl
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        initLog()
    }

    private fun initLog() {
        Timber.plant(DebugTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().appConfig(appConfig()).build()
    }

    private fun appConfig() = AppConfig(BuildConfig.SERVER_URL.toHttpUrl(), this)
}
