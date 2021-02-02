package app.sram.bikestore.di.network

import app.sram.bikestore.BuildConfig
import app.sram.bikestore.data.KEY_NAME
import app.sram.bikestore.di.AppScope
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
/*
*
* Auth key management.
* */
@AppScope
class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val url = chain.request().url.newBuilder()
            .addQueryParameter(KEY_NAME, BuildConfig.GOOGLE_KEY)
            .build()

        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}
