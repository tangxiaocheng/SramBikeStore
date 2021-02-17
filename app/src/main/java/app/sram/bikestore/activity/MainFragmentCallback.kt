package app.sram.bikestore.activity

import app.sram.bikestore.data.BikeStoreItem
import app.sram.bikestore.data.ErrorModel
import app.sram.bikestore.data.NetworkErrorCallback

interface MainFragmentCallback : NetworkErrorCallback {
    fun onServerError(errorModel: ErrorModel)
    fun onResultReady(list: List<BikeStoreItem>)
}
