package app.sram.bikestore.di.thread

import app.sram.bikestore.di.AppScope
import dagger.Binds
import dagger.Module
import java.util.concurrent.Executor

@Module
abstract class ThreadModule {
    @Binds
    @AppScope
    abstract fun provideThreadExecutor(jobExecutor: JobExecutor): Executor
}
