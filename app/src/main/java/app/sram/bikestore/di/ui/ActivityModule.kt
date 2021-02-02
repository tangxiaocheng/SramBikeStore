package app.sram.bikestore.di.ui

import app.sram.bikestore.DeviceLocationFragment
import app.sram.bikestore.activity.MainFragment
import dagger.android.ContributesAndroidInjector

@dagger.Module
abstract class ActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeDeviceLocationFragment(): DeviceLocationFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [MainFragment.Module::class])
    abstract fun contributeMainFragment(): MainFragment
}
