package app.sram.bikestore.di.thread

import app.sram.bikestore.di.AppScope
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

@Module(includes = [ThreadModule::class])
class
SchedulerModule {

    @AppScope
    @Provides
    @UIScheduler
    fun provideUIScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @AppScope
    @Provides
    @IOScheduler
    fun provideIOScheduler(executor: Executor): Scheduler {
        return Schedulers.from(executor)
    }
}
