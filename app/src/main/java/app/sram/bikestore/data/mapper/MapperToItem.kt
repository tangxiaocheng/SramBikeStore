package app.sram.bikestore.data.mapper

interface MapperToItem<out V, in D> {
    fun mapToItem(entity: D, distance: Float): V
}
