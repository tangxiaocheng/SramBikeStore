package app.sram.bikestore.di

import app.sram.bikestore.App
import app.sram.bikestore.BikeStoreDetailActivity
import app.sram.bikestore.di.network.RepoModule
import app.sram.bikestore.di.thread.SchedulerModule
import app.sram.bikestore.di.room.RoomModule
import app.sram.bikestore.di.ui.ActivityInjector
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@AppScope
@Singleton
@Component(
    modules = [
        ActivityInjector::class,
        RepoModule::class,
        AppConfigModule::class,
        SchedulerModule::class,
        RoomModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    fun inject(activity: BikeStoreDetailActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appConfig(config: AppConfig): Builder
        fun build(): AppComponent
    }
}
