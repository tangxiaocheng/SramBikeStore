package app.sram.bikestore.data

interface NetworkErrorCallback {
    fun onNetworkError(throwable: Throwable)
}
