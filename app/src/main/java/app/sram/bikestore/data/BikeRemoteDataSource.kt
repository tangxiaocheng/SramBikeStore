package app.sram.bikestore.data

import app.sram.bikestore.util.modelToEntity
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

/**
 * This repository provides a single source of truth for the raw data list. It does not handles any
 * filter logic. It does not address any specific business logic.
 * The remote source only fetch the data from network. It maps the raw http data to [ResultModel]. It does not handle the cache, etc.
 */
class BikeRemoteDataSource @Inject constructor(private val api: RestApi, private val pageTokenRepo: PageTokenRepo) {

    fun fetch(location: Param): Single<ResultModel<BikeStoreData>> {
        return fetchInternal(location)
    }

    private fun fetchInternal(param: Param): Single<ResultModel<BikeStoreData>> {
        val pageToken = param.pageToken.value
        return if (pageToken == null) {
            api.getBookStoreList(param.location.toQueryString()).map(this::mapToResultModel)
        } else {
            api.bikeStoreList(pageToken).map(this::mapToResultModel)
        }
    }

    private fun mapToResultModel(response: Response<MapApiResponse>): ResultModel<BikeStoreData> {
        return if (response.isSuccessful && response.body() != null) {
            val body: MapApiResponse = response.body()!!
            if (body.status == API_CODE_OK) {
                pageTokenRepo.put(body.nextPageToken)
                Success(BikeStoreData(body.results.map { modelToEntity(it) }))
            } else {
                Failure(ErrorModel(body.status, body.errorMessage, HTTP_CODE_OK))
            }
        } else {
            Failure(ErrorModel(null, response.errorBody()?.string(), response.code()))
        }
    }
}
