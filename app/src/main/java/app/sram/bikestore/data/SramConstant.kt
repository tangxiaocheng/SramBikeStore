package app.sram.bikestore.data

/*https://developers.google.com/places/web-service/search#PlaceSearchStatusCodes*/
const val HTTP_CODE_OK = 200
const val API_CODE_OK = "OK"
const val API_CODE_ZERO_RESULTS = "ZERO_RESULTS" // may need this one for Paging.
const val API_CODE_OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT"
const val API_CODE_REQUEST_DENIED = "REQUEST_DENIED"
const val API_CODE_INVALID_REQUEST = "INVALID_REQUEST"
const val API_CODE_UNKNOWN_ERROR = "UNKNOWN_ERROR"
val HOME = SramLocation(lat = 41.8864257269706, lng = -87.86843300745929)
// val HOME = ScramLocation(lat = 47.584166043000366, lng = -122.15145292817319)
val Chicago = SramLocation(lat = 41.8864257269706, lng = -87.86843300745929) // 41.8864257269706, -87.86843300745929
const val KEY_NAME = "key"
const val ARG_LOCATION = "ScramLocation"
const val PHOTO_HOLDER_URL =
    "https://png.pngtree.com/png-clipart/20190520/original/pngtree-placeholder-vector-icon-png-image_3725418.jpg"
