package app.sram.bikestore.data

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.sram.bikestore.di.room.DBConstant
import kotlinx.parcelize.Parcelize

// For persisting the data, I'm considering to flatten and filter out all the unwanted info from the original json object.
// Json object -> Room entity -> Adapter Item Model.
// For offerLine use: select * from db.
@Entity(tableName = DBConstant.TABLE_NAME)
data class BikeStoreEntity(
    @PrimaryKey(autoGenerate = false)
    val placeId: String,
    val name: String,
    val businessStatus: String?,
    val icon: String,
    val userRatingsTotal: Int,
    val priceLevel: Int,
    val rating: Float,
    val vicinity: String,
    @Embedded
    val location: ScramLocation,
    @Embedded
    val photo: Photo?
)
