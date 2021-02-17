package app.sram.bikestore.data.mapper

interface MapperToEntity<out V, in D> {
    fun mapToEntity(bean: D): V
}
