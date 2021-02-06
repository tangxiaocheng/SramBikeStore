package app.sram.bikestore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import app.sram.bikestore.data.BikeStoreItem
import app.sram.bikestore.databinding.ActivityBikeStoreDetailBinding
import app.sram.bikestore.util.formatDistance
import app.sram.bikestore.util.photoUrl
import coil.load

/*
* An activity display the detail information of a BikeStore.
* It only receives a [app.sram.bikestore.data.BikeStoreItem] argument.
* */
class BikeStoreDetailActivity : AppCompatActivity() {
    companion object {
        const val ARG_STORE_ITEM = "KEY_BIKE_STORE_ITEM"
    }

    private lateinit var bikeStoreItem: BikeStoreItem
    private lateinit var binding: ActivityBikeStoreDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.let {
            bikeStoreItem = it.getParcelableExtra(ARG_STORE_ITEM)!!
        }
        initView()
        bindData()
    }

    private fun initView() {
        binding = ActivityBikeStoreDetailBinding.inflate(layoutInflater)
        setSupportActionBar(binding.detailActivityToolbar)
        setContentView(binding.root)
    }

    private fun bindData() {

        binding.storeDistTv.text = formatDistance(bikeStoreItem.distance)
        bikeStoreItem.apply {
            binding.storePhotoIv.load(photoUrl)
            binding.storeNameTv.text = name
            binding.storeAddressTv.text = vicinity
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.map_fragment_view,
                    GoogleMapWrapperFragment.newInstance(location)
                )
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareToEmail(
                "Check out ${bikeStoreItem.name} ",
                "This cool store's address is ${bikeStoreItem.vicinity} "
            )
            else -> {
            }
        }
        return true
    }

    private fun shareToEmail(title: String, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            type = "text/html"
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, subject)
        }
        if (packageManager != null && intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}
