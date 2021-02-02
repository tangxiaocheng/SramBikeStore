package app.sram.bikestore.activity

import app.sram.bikestore.data.BikeStoreItem
import app.sram.bikestore.data.ErrorModel
import app.sram.bikestore.data.NetworkErrorCallback
import io.reactivex.Observable

interface MainFragmentCallback : NetworkErrorCallback {
    fun onServerError(errorModel: ErrorModel)
    fun loadingMoreSignal(): Observable<Unit>
    fun onResultReady(list: List<BikeStoreItem>)
}
