package app.sram.bikestore.util

import android.location.Location
import app.sram.bikestore.data.SramLocation

fun SramLocation.calculate(location: SramLocation): Float {
    val resultHolder = FloatArray(1) { 0f }
    Location.distanceBetween(
        lat,
        lng,
        location.lat,
        location.lng,
        resultHolder
    )
    return resultHolder[0]
}

// Used Google Map as a reference.
fun formatDistance(floatValue: Float): String {
    return "${(floatValue / 1609).format(1)} mi"
}

fun Float.format(digits: Int) = "%.${digits}f".format(this)

fun Location.toScramLocation(): SramLocation {
    return SramLocation(lat = this.latitude, lng = this.longitude)
}
