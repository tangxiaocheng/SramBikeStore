package app.sram.bikestore.data

interface BikeRepo {
    fun fetAllPages(location: ScramLocation, refresh: Boolean): ResultModel<BikeStoreData>
}
