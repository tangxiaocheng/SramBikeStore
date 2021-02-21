package app.sram.bikestore.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.sram.bikestore.GoogleMapWrapperFragment
import app.sram.bikestore.R
import app.sram.bikestore.data.*
import app.sram.bikestore.databinding.FragmentMainBinding
import app.sram.bikestore.di.ui.FragmentScope
import com.google.android.material.snackbar.Snackbar
import com.uber.autodispose.ScopeProvider
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import dagger.Provides
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

class MainFragment : DaggerFragment(), MainFragmentCallback {

    companion object {
        fun newInstance(location: SramLocation) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_LOCATION, location)
                }
            }
    }

    private var location: SramLocation = HOME

    @Inject
    lateinit var fragmentPresenter: MainFragmentPresenter

    @Inject
    lateinit var adapter: BikeStoreListAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var binding: FragmentMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            location = it.getParcelable(ARG_LOCATION) ?: HOME
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    private fun initView() {
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.adapter = adapter
        binding.refreshSrl.setColorSchemeResources(R.color.colorAccent)
        binding.refreshSrl.setOnRefreshListener {
            adapter.fresh()
            loadData(location, true)
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.map_fragment_view, GoogleMapWrapperFragment.newInstance(location)).commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        loadData(location, false)
    }

    /**
     * Using [com.uber.autodispose.AutoDispose] to provide the scope of binding result from server.
     * Currently, the life cycle scope is from onCreate to onDestroy.
     * This means, if the activity is destroyed, the network request will be cancelled automatically.
     * If we want to refresh the data every time when the app is brought to foreground, we could simply put this method onResume.
     * In this case, the network will be automatically cancelled upon onPause.
     */
    private fun loadData(location: SramLocation, refresh: Boolean) {
        fragmentPresenter.loadData(location, refresh)
    }

    override fun onResultReady(list: List<BikeStoreItem>) {
        bindList(list)
        updateView()
    }

    private fun bindList(list: List<BikeStoreItem>) {
        adapter.append(list)
    }

    private fun updateView() {
        if (adapter.itemCount == 0) {
            showEmptyView()
        } else {
            showListView()
        }
    }

    private fun showListView() {
        binding.refreshSrl.isRefreshing = false
        binding.loadingPb.visibility = View.GONE
        binding.tipTv.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun showEmptyView() {
        binding.refreshSrl.isRefreshing = false
        binding.loadingPb.visibility = View.GONE
        binding.tipTv.visibility = View.VISIBLE
        binding.tipTv.text = getString(R.string.no_near_by_bike_store)
        binding.recyclerView.visibility = View.GONE
    }

    override fun onServerError(errorModel: ErrorModel) {
        binding.refreshSrl.isRefreshing = false
        val errorMessage = "${errorModel.apiStatusCode} : ${errorModel.message}"
        showErrorMessage(errorMessage)
        showSnackBar(errorMessage)
    }

    override fun onNetworkError(throwable: Throwable) {
        Timber.e(throwable)
        showErrorMessage(getString(R.string.network_error_tip))
    }

    private fun showErrorMessage(errorMessage: String) {
        binding.loadingPb.visibility = View.GONE
        binding.tipTv.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.tipTv.text = errorMessage
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.tipTv, message, Snackbar.LENGTH_LONG)
            .setAction("RETRY") { loadData(location, false) }.show()
    }

    @dagger.Module
    class Module {
        @Provides
        @FragmentScope
        fun mainView(fragmentApp: MainFragment): MainFragmentCallback {
            return fragmentApp
        }

        @Provides
        @FragmentScope
        fun scopeProvider(fragmentApp: MainFragment): ScopeProvider {
            return AndroidLifecycleScopeProvider.from(fragmentApp)
        }

        @Provides
        @FragmentScope
        fun linearLayoutManager(fragmentApp: MainFragment): LinearLayoutManager {
            return LinearLayoutManager(fragmentApp.context)
        }
    }
}
