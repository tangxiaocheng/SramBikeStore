package app.sram.bikestore.di.room

import androidx.room.Database
import androidx.room.RoomDatabase
import app.sram.bikestore.data.BikeStoreEntity

@Database(entities = [BikeStoreEntity::class], version = DBConstant.DB_VERSION, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): DataBaseDao
}
