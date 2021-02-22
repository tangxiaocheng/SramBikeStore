package app.sram.bikestore.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BikeStoreItem(
    val distance: Float,
    val formatDistance: String,
    val photoUrl: String,
    val name: String,
    val rating: Float,
    val userRatingsTotal: Int,
    val vicinity: String,
    val location: SramLocation
) : Parcelable
