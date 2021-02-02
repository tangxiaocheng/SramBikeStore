package app.sram.bikestore.data

import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class BikeRepoImpl @Inject constructor(
    private val bikeRemoteDataSource: BikeRemoteDataSource,
    private val bikeDatabaseSource: BikeDatabaseSource
) : BikeRepo {

    /**
     * Try to fetch data from network first as we want to show the up-to-date data to the user.
     * If the network is successful, we save the data into database.
     * If the network fails, we filter it out and fallback the data source onto the database.
     *
     * As a [Maybe.concat] operation here, the order matters here.
     * The [firstElement] would be [remoteSource] if it successes.
     */
    override fun list(param: Param): Single<ResultModel<BikeStoreData>> {
        return Maybe.concat(remoteSource(param).filter { it is Success }, databaseSource()).firstElement().toSingle()
    }

    private fun databaseSource(): Maybe<ResultModel<BikeStoreData>> {
        return bikeDatabaseSource.list()
    }

    private fun remoteSource(param: Param): Single<ResultModel<BikeStoreData>> {
        return bikeRemoteDataSource
            .fetch(param)
            .doOnSuccess { cacheToDatabase(it) }
            .onErrorReturn { Failure(ErrorModel(null, it.message, NETWORK_DISCONNECTED)) }
    }
/*
* The cache policy here is always storing the data by the [BikeStoreEntity.placeId] as primary key
* onConflict = OnConflictStrategy.REPLACE
*
* */
    private fun cacheToDatabase(resultModel: ResultModel<BikeStoreData>) {
        if (resultModel is Success) {
            bikeDatabaseSource.insertAll(resultModel.data.list)
        }
    }
}
