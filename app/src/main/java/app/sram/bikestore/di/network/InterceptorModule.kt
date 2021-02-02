package app.sram.bikestore.di.network

import android.content.Context
import app.sram.bikestore.di.AppScope
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.simplemented.okdelay.DelayInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
class
InterceptorModule {

    @AppScope
    @Provides
    fun provideChuckerInterceptor(context: Context): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context).build()
    }

    @AppScope
    @Provides
    fun provideDelayInterceptor(): DelayInterceptor {
        return DelayInterceptor(0, TimeUnit.MILLISECONDS)
    }

    @AppScope
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC
        return logger
    }
}
