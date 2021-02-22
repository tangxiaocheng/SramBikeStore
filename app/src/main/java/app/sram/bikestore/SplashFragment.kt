package app.sram.bikestore

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import app.sram.bikestore.data.ARG_LOCATION
import app.sram.bikestore.data.HOME
import app.sram.bikestore.data.SramLocation
import app.sram.bikestore.databinding.FragmentSplashBinding
import com.google.android.material.snackbar.Snackbar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider

class SplashFragment : Fragment() {
    lateinit var viewForSnackBar: View
    lateinit var binding: FragmentSplashBinding
    private val requiredPermission = Manifest.permission.ACCESS_COARSE_LOCATION

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater)
        viewForSnackBar = binding.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kickOffMainFlow(AndroidLifecycleScopeProvider.from(this))
    }

    private fun kickOffMainFlow(scopeProvider: ScopeProvider) {
        RxPermissions(this)
            .request(requiredPermission)
            .`as`(AutoDispose.autoDisposable(scopeProvider))
            .subscribe { granted ->
                if (granted) {
                    navigateToDeviceLocationFragment()
                } else {
                    onLocationPermissionDenied()
                }
            }
    }

    private fun navigateToDeviceLocationFragment() {
        findNavController().navigate(
            R.id.deviceLocationFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
        )
    }

    private fun onLocationPermissionDenied() {
        showSnackBar(getString(R.string.apply_default_location))
        navigateToMainFragment(HOME)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(viewForSnackBar, message, Snackbar.LENGTH_LONG).show()
    }

    private fun navigateToMainFragment(sramLocation: SramLocation) {

        findNavController().navigate(
            R.id.mainFragment,
            bundleOf(ARG_LOCATION to sramLocation),
            NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
        )
    }
}
