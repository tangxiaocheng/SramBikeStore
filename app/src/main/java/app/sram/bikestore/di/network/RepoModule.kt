package app.sram.bikestore.di.network

import app.sram.bikestore.data.*
import app.sram.bikestore.di.AppScope
import dagger.Binds
import dagger.Module

@Module(includes = [RestApiModule::class])
abstract class RepoModule {

    @Binds
    @AppScope
    abstract fun bindBikeRepo(impl: BikeRepoImp): BikeRepo
}
