package app.sram.bikestore.di.ui

import app.sram.bikestore.activity.MainActivity
import app.sram.bikestore.BikeStoreDetailActivity
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector

@Module(includes = [AndroidInjectionModule::class])
abstract class ActivityInjector {

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeBikeStoreDetailActivity(): BikeStoreDetailActivity
}
