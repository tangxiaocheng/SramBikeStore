package app.sram.bikestore.data.mapper

import app.sram.bikestore.data.BikeStoreEntity
import app.sram.bikestore.data.BikeStoreItem
import app.sram.bikestore.data.PHOTO_HOLDER_URL
import app.sram.bikestore.util.formatDistance
import app.sram.bikestore.util.photoUrl
import javax.inject.Inject

class EntityToItemMapper @Inject constructor() : MapperToItem<BikeStoreItem, BikeStoreEntity> {
    override fun mapToItem(entity: BikeStoreEntity, distance: Float): BikeStoreItem {
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
}
