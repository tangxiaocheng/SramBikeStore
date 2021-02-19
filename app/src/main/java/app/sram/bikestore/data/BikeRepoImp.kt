package app.sram.bikestore.data

import javax.inject.Inject

class BikeRepoImp @Inject constructor(
    private val remoteDataSource: BikeRemoteDataSource,
    private val localDataSource: BikeDatabaseSource
) : BikeRepo {

    private fun localDataSource(): ResultModel<BikeStoreData> {
        return localDataSource.listAllPages()
    }

    override fun list(location: ScramLocation, refresh: Boolean): ResultModel<BikeStoreData> {
        return if (refresh) {
            // May be we can add the logic to clear local database first when the user is refreshing.
            fetchFromRemote(location)
        } else {
            val localData: ResultModel<BikeStoreData> = localDataSource()
            if (localData is Success) {
                localData
            } else {
                fetchFromRemote(location)
            }
        }
    }

    private fun fetchFromRemote(location: ScramLocation): ResultModel<BikeStoreData> {
        val remoteData = remoteDataSource.fetch(location)
        saveToLocalDatabase(remoteData) // side effect that save data to local database.
        return remoteData
    }

    private fun saveToLocalDatabase(remoteData: ResultModel<BikeStoreData>) {
        if (remoteData is Success) {
            remoteData.data.let { localDataSource.insertAll(it.list) }
        }
    }
}
