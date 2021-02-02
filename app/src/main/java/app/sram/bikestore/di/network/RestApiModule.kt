package app.sram.bikestore.di.network

import app.sram.bikestore.data.RestApi
import app.sram.bikestore.di.AppScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [RetrofitModule::class])
class RestApiModule {
    @Provides
    @AppScope
    fun provideApi(retrofit: Retrofit): RestApi {
        return retrofit.create(RestApi::class.java)
    }
}
