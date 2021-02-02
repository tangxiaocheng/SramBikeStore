package app.sram.bikestore.di.room

import androidx.room.*
import app.sram.bikestore.data.BikeStoreEntity
import io.reactivex.Single

@Dao
interface DataBaseDao {

    @Query("SELECT * FROM ${DBConstant.TABLE_NAME} ORDER BY placeId ASC")
    fun getBikeStoreList(): List<BikeStoreEntity>

    @Query("SELECT * FROM ${DBConstant.TABLE_NAME} ORDER BY placeId ASC")
    fun getBikeStoreListSingle(): Single<List<BikeStoreEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBikeStore(bikeStoreEntity: BikeStoreEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllBikeStore(list: List<BikeStoreEntity>): List<Long>
}
