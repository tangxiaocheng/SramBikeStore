package app.sram.bikestore.activity

import app.sram.bikestore.util.calculate
import app.sram.bikestore.data.*
import app.sram.bikestore.util.entityToItem
import app.sram.bikestore.di.thread.IOScheduler
import app.sram.bikestore.di.thread.UIScheduler
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject

/**
 * The use case conducts the business logic such as the sorting here. It does not care about how the repository is providing the data.
 * It could consume multiple repository.
 */
class BikeStoreListUseCase @Inject constructor(
    private val bikeRepo: BikeRepo,
    @IOScheduler private val ioScheduler: Scheduler,
    @UIScheduler private val mainScheduler: Scheduler
) {
    fun execute(param: Param): Single<ResultModel<List<BikeStoreItem>>> {
        return bikeRepo.list(param).map { mapToAdapterModel(it, param.location) }.subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
    }

    private fun mapToAdapterModel(resultModel: ResultModel<BikeStoreData>, deviceLocation: ScramLocation):
        ResultModel<List<BikeStoreItem>> {

            return when (resultModel) {
                is Success -> Success(
                    resultModel.data.list.map {
                        entityToItem(it, deviceLocation.calculate(it.location))
                    }.sortedBy { it.distance }
                )
                is Failure -> Failure(resultModel.errorModel)
            }
        }
}
