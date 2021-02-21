package app.sram.bikestore.data

interface BikeRepo {
    fun list(location: SramLocation, refresh: Boolean): ResultModel<BikeStoreData>
}
