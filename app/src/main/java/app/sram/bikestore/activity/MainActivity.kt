package app.sram.bikestore.activity

import android.os.Bundle
import android.view.View
import app.sram.bikestore.R
import app.sram.bikestore.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.uber.autodispose.ScopeProvider
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider.from
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var networkStatusHelper: NetworkStatusHelper
    lateinit var binding: ActivityMainBinding
    lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.mainActivityToolbar)
        view = binding.root
        setContentView(binding.root)
        observeNetworkStatus(from(this))
    }

    private fun observeNetworkStatus(scopeProvider: ScopeProvider) {
        networkStatusHelper.observe(scopeProvider, this::onNetworkStatusUpdated)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    private fun onNetworkStatusUpdated(isConnectedToInternet: Boolean) {
        if (!isConnectedToInternet) {
            showSnackBar(resources.getString(R.string.network_disconnected))
        }
    }
}
