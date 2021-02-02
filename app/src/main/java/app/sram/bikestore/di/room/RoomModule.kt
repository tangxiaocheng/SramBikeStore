package app.sram.bikestore.di.room

import android.content.Context
import androidx.room.Room
import app.sram.bikestore.di.AppScope
import dagger.Module
import dagger.Provides

@Module
class RoomModule {
    @AppScope
    @Provides
    fun provideDao(database: AppDatabase): DataBaseDao {
        return database.getDao()
    }

    @AppScope
    @Provides
    fun provideDataBase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DBConstant.DB_NAME
        )
            .fallbackToDestructiveMigration()
//            .allowMainThreadQueries()
            .build()
    }
}
