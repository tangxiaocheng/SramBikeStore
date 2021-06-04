package app.sram.bikestore

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import app.sram.bikestore.data.ARG_LOCATION
import app.sram.bikestore.data.HOME
import app.sram.bikestore.databinding.FragmentDeviceLocationBinding
import app.sram.bikestore.di.ui.FragmentScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Provides
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/*
* Separation of concerns: In this Fragment, it only handles the logic of getting the location.
* If it fails in getting the last location, the app will prompt to apply for the default sample location.
*
* */
class DeviceLocationFragment : DaggerFragment() {

    @Inject
    lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDeviceLocationBinding.inflate(inflater).root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient.lastLocation.addOnSuccessListener { updateLocation(it) }
    }

    private fun updateLocation(location: Location?) {
        findNavController().navigate(
            R.id.mainFragment,
            bundleOf(ARG_LOCATION to (location ?: HOME)),
            NavOptions.Builder().setPopUpTo(R.id.deviceLocationFragment, true).build()
        )
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
