package app.sram.bikestore.data

interface BikeRepo {
    fun list(location: ScramLocation, refresh: Boolean): ResultModel<BikeStoreData>
}
