package app.sram.bikestore.data

interface AllPagesRepo {
    fun fetAllPages(location: ScramLocation): ResultModel<BikeStoreData>
}
