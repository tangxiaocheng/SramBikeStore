package app.sram.bikestore

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import app.sram.bikestore.DeviceLocationFragment.Companion.REQUEST_KEY_LOCATION
import app.sram.bikestore.activity.MainFragment
import app.sram.bikestore.data.HOME
import app.sram.bikestore.data.ScramLocation
import app.sram.bikestore.databinding.FragmentSplashBinding
import com.google.android.material.snackbar.Snackbar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider

class SplashFragment : Fragment() {

    lateinit var viewForSnackBar: View
    lateinit var binding: FragmentSplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind(AndroidLifecycleScopeProvider.from(this))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSplashBinding.inflate(inflater)
        viewForSnackBar = binding.root
        return binding.root
    }

    private val requiredPermission = Manifest.permission.ACCESS_COARSE_LOCATION

    private fun bind(scopeProvider: ScopeProvider) {
        kickOffMainFlow(scopeProvider)
    }

    private fun bindDeviceLocationFragment() {
        setFragmentResultListener(REQUEST_KEY_LOCATION) { _: String, bundle ->
            val location = bundle.getParcelable<ScramLocation>(DeviceLocationFragment.LOCATION_BUNDLE_KEY)!!
            bindMainFragment(location)
        }

        val fragment = DeviceLocationFragment.newInstance()
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun kickOffMainFlow(scopeProvider: ScopeProvider) {
        RxPermissions(this)
            .request(requiredPermission)
            .`as`(AutoDispose.autoDisposable(scopeProvider))
            .subscribe { granted ->
                if (granted) {
                    bindDeviceLocationFragment()
                } else {
                    onLocationPermissionDenied()
                }
            }
    }

    private fun onLocationPermissionDenied() {
        showSnackBar(getString(R.string.apply_default_location))
        bindMainFragment(HOME)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(viewForSnackBar, message, Snackbar.LENGTH_LONG).show()
    }

    private fun bindMainFragment(scramLocation: ScramLocation) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment.newInstance(scramLocation))
            .commit()
    }
}
