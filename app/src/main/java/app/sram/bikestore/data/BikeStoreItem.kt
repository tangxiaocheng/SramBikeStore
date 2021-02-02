package app.sram.bikestore.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BikeStoreItem(
    val bikeStoreEntity: BikeStoreEntity,
    val distance: Float

) : Parcelable
