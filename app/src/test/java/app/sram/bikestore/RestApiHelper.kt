package app.sram.bikestore

import app.sram.bikestore.data.RestApi
import app.sram.bikestore.di.network.AuthInterceptor
import app.sram.bikestore.di.network.MySyncCallAdapterFactory
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RestApiHelper {
    private fun providesRetrofit(url: HttpUrl): Retrofit {

        return Retrofit.Builder()
            .client(okHttpClient())
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .addCallAdapterFactory(MySyncCallAdapterFactory())

            .baseUrl(url)
            .build()
    }

    private fun okHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(AuthInterceptor())
        return builder.build()
    }

    private val rxJava2CallAdapterFactory by lazy {
        RxJava2CallAdapterFactory.create()
    }

    private val gsonConverterFactory: GsonConverterFactory by lazy {
        GsonConverterFactory.create(
            GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
        )
    }

    fun getRestApi(url: HttpUrl): RestApi {
        return providesRetrofit(url).create(RestApi::class.java)
    }
}
