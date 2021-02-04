package app.sram.bikestore.activity

import android.Manifest
import android.os.Bundle
import android.view.View
import app.sram.bikestore.BlankFragment
import app.sram.bikestore.DeviceLocationFragment
import app.sram.bikestore.R
import app.sram.bikestore.data.HOME
import app.sram.bikestore.data.ScramLocation
import app.sram.bikestore.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider.from
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), DeviceLocationFragment.Callback {

    private val requiredPermission = Manifest.permission.ACCESS_COARSE_LOCATION

    @Inject
    lateinit var networkStatusHelper: NetworkStatusHelper

    lateinit var binding: ActivityMainBinding
    lateinit var view:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        bind(from(this))
    }

    private fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.mainActivityToolbar)
        view = binding.container
        setContentView(binding.root)
    }

    private fun bind(scopeProvider: ScopeProvider) {
        observeNetworkStatus(scopeProvider)
        kickOffMainFlow(scopeProvider)
    }

    private fun observeNetworkStatus(scopeProvider: ScopeProvider) {
        networkStatusHelper.observe(scopeProvider, this::onNetworkStatusUpdated)
    }

    private fun bindDeviceLocationFragment() {
        val fragment = DeviceLocationFragment.newInstance()
        fragment.setCallback(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun onLocationReady(location: ScramLocation) {
        bindMainFragment(location)
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

    private fun showSnackBar( message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    private fun onNetworkStatusUpdated( isConnectedToInternet: Boolean) {
        if (!isConnectedToInternet) {
            showSnackBar(resources.getString(R.string.network_disconnected))
        }
    }

    private fun bindMainFragment(scramLocation: ScramLocation) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment.newInstance(scramLocation))
//            .replace(R.id.container, BlankFragment.newInstance("hello","world"))
            .commit()
    }
}
