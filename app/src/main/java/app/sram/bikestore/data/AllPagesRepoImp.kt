package app.sram.bikestore.data

import javax.inject.Inject

class AllPagesRepoImp @Inject constructor(
    private val bikeRemoteDataSource: AllPagesRemoteRepo,
    private val bikeDatabaseSource: BikeDatabaseSource
) : AllPagesRepo {

    private fun databaseSource(): ResultModel<BikeStoreData> {
        return bikeDatabaseSource.listAllPages()
    }

    override fun fetAllPages(location: ScramLocation): ResultModel<BikeStoreData> {
        val fetAllPages: ResultModel<BikeStoreData> = bikeRemoteDataSource.fetAllPages(location)
        return if (fetAllPages is Success) {
            bikeDatabaseSource.insertAll(fetAllPages.data.list)
            fetAllPages
        } else {
            databaseSource()
        }
    }
}
