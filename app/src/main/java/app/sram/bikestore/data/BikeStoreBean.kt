package app.sram.bikestore.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class BikeStoreBean(
    val businessStatus: String?,
    val icon: String,
    val geometry: Geometry,
    val name: String,
    val vicinity: String,
    val rating: Float,
    val photos: List<Photo>?,
    val reference: String,
    val userRatingsTotal: Int,
    val priceLevel: Int,
    val placeId: String
)

data class Geometry(
    val location: SramLocation
)

data class Photo(
    val photoReference: String,
    val width: Int,
    val height: Int
)

@Parcelize
data class SramLocation(
    val lat: Double,
    val lng: Double
) : Parcelable {
    fun toQueryString(): String {
        return "$lat, $lng"
    }
}

data class Token(
    val value: String? = null
)

data class Param(
    val location: SramLocation,
    val pageToken: Token = Token()
)
