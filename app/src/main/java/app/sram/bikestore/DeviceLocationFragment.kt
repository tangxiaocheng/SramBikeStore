package app.sram.bikestore

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.sram.bikestore.data.HOME
import app.sram.bikestore.data.ScramLocation
import app.sram.bikestore.di.ui.FragmentScope
import app.sram.bikestore.util.toScramLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Provides
import dagger.android.support.DaggerFragment
import java.lang.RuntimeException
import javax.inject.Inject

/*
* Separation of concerns: In this Fragment, it only handles the logic of getting the location.
* If it fails in getting the last location, the app will prompt to apply for the default sample location.
*
* */
class DeviceLocationFragment : DaggerFragment() {

    @Inject
    lateinit var fusedLocationClient: FusedLocationProviderClient
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
        fusedLocationClient.lastLocation.addOnSuccessListener { updateLocation(it) }
    }

    private fun updateLocation(location: Location?) {
        if (location == null) {
            if (this::callback.isInitialized) {
                callback.onLocationReady(HOME)
            } else {
                throw RuntimeException("callback has not been set")
            }
        } else {
            callback.onLocationReady(location.toScramLocation())
        }
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    interface Callback {
        fun onLocationReady(location: ScramLocation)
    }

    @dagger.Module
    class Module {

        @Provides
        @FragmentScope
        fun mainView(fragmentApp: DeviceLocationFragment): FusedLocationProviderClient {
            return LocationServices.getFusedLocationProviderClient(fragmentApp.requireActivity())
        }
    }
}
