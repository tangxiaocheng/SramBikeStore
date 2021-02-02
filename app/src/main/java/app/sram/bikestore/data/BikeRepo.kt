package app.sram.bikestore.data

import io.reactivex.Single

interface BikeRepo {
    fun list(param: Param): Single<ResultModel<BikeStoreData>>
}
