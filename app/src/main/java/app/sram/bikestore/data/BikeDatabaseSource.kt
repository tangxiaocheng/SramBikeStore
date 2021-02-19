package app.sram.bikestore.data

import app.sram.bikestore.di.room.DataBaseDao
import timber.log.Timber
import javax.inject.Inject

/**
 * Database source to save remote data into database & provide an API to access it.
 */
class BikeDatabaseSource @Inject constructor(
    private val dao: DataBaseDao
) {

    fun insertAll(it: List<BikeStoreEntity>) {
        val list = dao.insertAllBikeStore(it)
        Timber.i("Inputted ${it.size}, Inserted ${list.size}")
    }

    fun listAllPages(): ResultModel<BikeStoreData> {
        val allPages = dao.getAllPages()
        return if (allPages.isNotEmpty()) {
            Success(BikeStoreData(allPages))
        } else {
            Failure(ErrorModel("", "local database is empty", 1000))
        }
    }
}
