package app.sram.bikestore.di.network

import app.sram.bikestore.di.AppScope
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * To create a [Retrofit], we need [OkHttpClient], hence we are including [OkHttpClientModule].
 * We also need [GsonModule], hence we are including [GsonModule].
 */
@Module(includes = [GsonModule::class, OkHttpClientModule::class])
class RetrofitModule {
    @AppScope
    @Provides
    fun provideRetrofit(
        serverUrl: HttpUrl,
        okHttpClient: OkHttpClient,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder().baseUrl(serverUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @AppScope
    @Provides
    fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @AppScope
    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }
}
