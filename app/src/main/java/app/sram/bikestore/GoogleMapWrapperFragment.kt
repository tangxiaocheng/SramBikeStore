package app.sram.bikestore

import android.os.Bundle
import android.view.View
import app.sram.bikestore.data.ARG_LOCATION
import app.sram.bikestore.data.HOME
import app.sram.bikestore.data.SramLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Wrap up map fragment:[SupportMapFragment].
 * Used in two place: [app.sram.bikestore.main.MainFragment] and [BikeStoreDetailActivity]
 * Only receive [SramLocation] as parameter.
 */
class GoogleMapWrapperFragment : SupportMapFragment(), OnMapReadyCallback {

    companion object {
        fun newInstance(location: SramLocation) =
            GoogleMapWrapperFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_LOCATION, location)
                }
            }
    }

    lateinit var latLng: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val location = it.getParcelable(ARG_LOCATION) ?: HOME
            latLng = LatLng(location.lat, location.lng)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.setAllGesturesEnabled(false)
        animateLocation(latLng, googleMap)
    }

    private fun animateLocation(latLng: LatLng, googleMap: GoogleMap) {
        googleMap.addMarker(MarkerOptions().position(latLng).title("Current Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.setMinZoomPreference(15.0f)
    }
}
