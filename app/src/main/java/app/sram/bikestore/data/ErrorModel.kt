package app.sram.bikestore.data
/*Google map api has a status code in its json object.
https://developers.google.com/places/web-service/search#PlaceSearchStatusCodes
"OK"  indicates that no errors occurred; the place was successfully detected and at least one result was returned. */
/**
 * [resultCode] represents http status code or domain code such as network disconnected [NETWORK_DISCONNECTED].
 * [apiStatusCode] represents the api status code which is in the result json string.
 */
const val NETWORK_DISCONNECTED = -1
data class ErrorModel(val apiStatusCode: String?, val message: String?, val resultCode: Int)
