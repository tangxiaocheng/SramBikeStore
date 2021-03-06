package app.sram.bikestore.util

import app.sram.bikestore.BuildConfig
import app.sram.bikestore.data.*

/*
* BikeStoreBean --> BikeStoreEntity --> BikeStoreItem
*      Json     -->    Database Dao --> Adapter Item.
* Keep the single responsibility principle for Object classes.
* In this case:
* 1, BikeStoreEntity should not mess with Parcelize. Exception for ScramLocation.
* 2, Processing all the computing logic here instead of computing it in the Adapter class.
* */
fun entityToItem(entity: BikeStoreEntity, distance: Float): BikeStoreItem {
    return BikeStoreItem(
        distance = distance,
        formatDistance = formatDistance(distance),
        photoUrl = entity.photo?.photoUrl() ?: PHOTO_HOLDER_URL,
        name = entity.name,
        rating = entity.rating,
        userRatingsTotal = entity.userRatingsTotal,
        vicinity = entity.vicinity,
        location = entity.location
    )
}

/**
 * google will transform the assemble url with photo reference into an different url, which makes the image cache not applicable at the coil library.
 *
 * Raw image url : https://maps.googleapis.com/maps/api/place/photo?maxheight=200&photoreference=ATtYBwJlJJB3CAGWBynHtJJ6R4Rtf6JWDf7VM6ZyUy7gmkDzagMdy0JBV9S8KcC7DeJDN0txL3J_AuB-fyKNIFgQF8mhjrUyOUr1Azk1iWvUsO0pThjBKwhc-XICBPZ5BMgsjs4Y0gH_VDlLXQW-9zmZts4emtKH9LCLJAw1dyO7GTWiTSHk&key=AIzaSyDdUF-Dqw2d_75kD7Q_0F6VuSe0iaGXBUQ
 * Redirected url : https://lh3.googleusercontent.com/p/AF1QipOqvB5m4oD9ChYgcV3usXCal1kZJGaOkoHi2NHR=s1600-h200
 */
fun Photo.photoUrl(): String {
    return "${BuildConfig.SERVER_URL}maps/api/place/photo?maxheight=200&photoreference=" +
        "$photoReference&$KEY_NAME=${BuildConfig.GOOGLE_KEY}"
}

fun modelToEntity(bikeStoreBean: BikeStoreBean): BikeStoreEntity {
    return BikeStoreEntity(
        placeId = bikeStoreBean.placeId,
        name = bikeStoreBean.name,
        businessStatus = bikeStoreBean.businessStatus,
        icon = bikeStoreBean.icon,
        userRatingsTotal = bikeStoreBean.userRatingsTotal,
        priceLevel = bikeStoreBean.priceLevel,
        rating = bikeStoreBean.rating,
        vicinity = bikeStoreBean.vicinity,
        location = bikeStoreBean.geometry.location,
        photo = bikeStoreBean.photos?.get(0)
    )
}
