package app.sram.bikestore.di.network

import app.sram.bikestore.di.AppScope
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.simplemented.okdelay.DelayInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * 1, [OkHttpClient] could be shared.
 *
 * 2, It consumes a lot of resources to create a [OkHttpClient] channel.
 * Hence, marking it as {[AppScope] will save the memory resources.
 */
@Module(includes = [InterceptorModule::class])
class
OkHttpClientModule {

    @AppScope
    @Provides
    fun provideOkHttpClient(
        chuckerInterceptor: ChuckerInterceptor,
        delayInterceptor: DelayInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {

        val builder = OkHttpClient.Builder()
        builder.addInterceptor(authInterceptor)
        builder.addInterceptor(chuckerInterceptor)
        builder.addInterceptor(loggingInterceptor)
        builder.addInterceptor(delayInterceptor)
        return builder.build()
    }
}
