package app.sram.bikestore.data
/*
* Json bean for parse the API response.
* */
data class MapApiResponse(
    val status: String,
    val results: List<BikeStoreBean>,
    val nextPageToken: String?,
    val error_message: String?
)
