package app.sram.bikestore

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.sram.bikestore.data.HOME
import app.sram.bikestore.data.ScramLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
/*
* Separation of concerns: In this Fragment, it only handles the logic of getting the location.
* If it fails in getting the last location, the app will prompt to apply for the default sample location.
*
* */
class DeviceLocationFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var callback: Callback

    companion object {
        fun newInstance() = DeviceLocationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_device_location, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { updateLocation(it) }
    }

    private fun updateLocation(location: Location?) {
        if (location == null) {
            callback.onLocationReady(HOME)
        } else {
            callback.onLocationReady(toScramLocation(location))
        }
    }

    private fun toScramLocation(location: Location): ScramLocation {
        return ScramLocation(lat = location.latitude, lng = location.longitude)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    interface Callback {
        fun onLocationReady(location: ScramLocation)
    }
}
