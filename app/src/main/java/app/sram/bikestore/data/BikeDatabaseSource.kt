package app.sram.bikestore.data

import app.sram.bikestore.di.room.DataBaseDao
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

/**
 * Database source to save remote data into database & provide an API to access it.
 */
class BikeDatabaseSource @Inject constructor(
    private val dao: DataBaseDao
) {

    fun list(): Maybe<ResultModel<BikeStoreData>> {
        return dao.getBikeStoreListSingle()
            .filter { it.isNotEmpty() }
            .map { Success(BikeStoreData(it)) }
    }

    fun insertAll(it: List<BikeStoreEntity>) {
        val list = dao.insertAllBikeStore(it)
        Timber.i("Inputted ${it.size}, Inserted ${list.size}")
    }
}
