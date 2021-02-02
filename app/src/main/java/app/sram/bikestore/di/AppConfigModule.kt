package app.sram.bikestore.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl

@Module
class AppConfigModule {
    @AppScope
    @Provides
    fun provideServerUrl(appConfig: AppConfig): HttpUrl {
        return appConfig.serverUrl
    }

    /**
     * Expose Application rather than context to avoid confusion from Activity Context.
     * Any dependencies that requests Application context could just use the Application in AppComponent
     * */
    @AppScope
    @Provides
    fun provideApplication(appConfig: AppConfig): Application {
        return appConfig.application
    }
    @AppScope
    @Provides
    fun provideContext(app: Application): Context {
        return app.applicationContext
    }

    @AppScope
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("page_token", Context.MODE_PRIVATE)
    }
}
