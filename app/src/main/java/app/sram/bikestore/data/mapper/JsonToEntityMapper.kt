package app.sram.bikestore.data.mapper

import app.sram.bikestore.data.BikeStoreBean
import app.sram.bikestore.data.BikeStoreEntity
import javax.inject.Inject

class JsonToEntityMapper @Inject constructor() : MapperToEntity<BikeStoreEntity, BikeStoreBean> {
    override fun mapToEntity(bean: BikeStoreBean): BikeStoreEntity {
        return BikeStoreEntity(
            placeId = bean.placeId,
            name = bean.name,
            businessStatus = bean.businessStatus,
            icon = bean.icon,
            userRatingsTotal = bean.userRatingsTotal,
            priceLevel = bean.priceLevel,
            rating = bean.rating,
            vicinity = bean.vicinity,
            location = bean.geometry.location,
            photo = bean.photos?.get(0)
        )
    }
}
