package app.sram.bikestore.data

import app.sram.bikestore.data.mapper.JsonToEntityMapper
import javax.inject.Inject

const val START_TOKEN = ""
const val END_TOKEN = "null"

class BikeRemoteDataSource @Inject constructor(
    private val api: RestApi,
    private val jsonToEntityMapper: JsonToEntityMapper
) {

    fun fetAllPages(location: ScramLocation): ResultModel<BikeStoreData> {

        val bikeList = ArrayList<BikeStoreBean>(20)
        var curToken = START_TOKEN
        var errorModel: ErrorModel? = null
        while (curToken != END_TOKEN) {
            val response: MapApiResponse = if (curToken == START_TOKEN) {
                api.getPageListByLocation(location.toQueryString())
            } else {
                api.getPageListByPageToken(curToken)
            }
            val (status: String, results: List<BikeStoreBean>, nextPageToken: String?, errorMessage: String?) = response
            if (status == API_CODE_OK) {
                bikeList.addAll(results)
            } else {
                errorModel = ErrorModel(status, errorMessage, 200)
                break
            }
            curToken = nextPageToken ?: END_TOKEN
            if (curToken != END_TOKEN) {
                // Interesting google map paging api:when you get a valid pageToken, you have to wait for a while to get the page, otherwise, it will return error.
                Thread.sleep(2000)
            }
        }

        return if (bikeList.isNotEmpty()) {
            Success(BikeStoreData(bikeList.map(jsonToEntityMapper::mapToEntity)))
        } else {
            Failure(errorModel!!)
        }
    }
}
