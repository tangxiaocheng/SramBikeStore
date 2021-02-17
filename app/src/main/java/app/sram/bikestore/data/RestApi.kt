package app.sram.bikestore.data

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * List all the endpoint defined by server and wrap it as retrofit interface. It could be auto generated.
 *
 * For google map api here, if a request has more than one page, it will return a next_page_token in the json.
 * For this paged api, we can just pass the [pagetoken] into to the query parameters.
 */

const val PATH = "maps/api/place/nearbysearch/json"

interface RestApi {
    /**
     * Be explicit of the error case and force the API consumers to handle such cases for better safety by return  a [Response] rather than the raw list.
     * The lower level of the code, the more explicit the code should be written.
     * So that the higher level, also known as consumer gets the chance how to handle it properly.
     */
    @GET(PATH)
    fun getBikeStoreListByLocation(
        @Query("location") location: String,
        @Query("radius") radius: String = "50000",
        @Query("type") type: String = "bicycle_store"
    ): Single<Response<MapApiResponse>>

    /*
    * For a request with a pagetoken, map api doesn't need other parameters here.
    * */
    @GET(PATH)
    fun getBikeStoreListByPageToken(
        @Query("pagetoken") pageToken: String
    ): Single<Response<MapApiResponse>>

    /*
    * Since the page token is in the result of the previous request
    * here I will use block-thread method. Initially, it starts in a Non-UI thread.
    *
    * */
    @GET(PATH)
    fun getPageListByLocation(
        @Query("location") location: String,
        @Query("radius") radius: String = "50000",
        @Query("type") type: String = "bicycle_store"
    ): MapApiResponse

    @GET(PATH)
    fun getPageListByPageToken(
        @Query("pagetoken") pageToken: String
    ): MapApiResponse
}
