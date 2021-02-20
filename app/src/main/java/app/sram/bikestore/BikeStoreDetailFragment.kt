package app.sram.bikestore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import app.sram.bikestore.data.BikeStoreItem
import app.sram.bikestore.databinding.FragmentBikeStoreDetailBinding
import app.sram.bikestore.util.formatDistance
import coil.load

class BikeStoreDetailFragment : Fragment() {

    companion object {
        const val ARG_STORE_ITEM = "KEY_BIKE_STORE_ITEM"
        fun newInstance(bikeStoreItem: BikeStoreItem) =
            BikeStoreDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_STORE_ITEM, bikeStoreItem)
                }
            }
    }

    private lateinit var bikeStoreItem: BikeStoreItem
    private lateinit var binding: FragmentBikeStoreDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            bikeStoreItem = it.getParcelable(ARG_STORE_ITEM)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBikeStoreDetailBinding.inflate(layoutInflater)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.detailActivityToolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
    }

    private fun bindData() {

        binding.storeDistTv.text = formatDistance(bikeStoreItem.distance)
        bikeStoreItem.apply {
            binding.storePhotoIv.load(photoUrl)
            binding.storeNameTv.text = name
            binding.storeAddressTv.text = vicinity
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.map_fragment_view,
                    GoogleMapWrapperFragment.newInstance(location)
                )
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share, menu)
        super.onCreateOptionsMenu(menu, inflater)
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
        if (requireActivity().packageManager != null &&
            intent.resolveActivity(requireActivity().packageManager) != null
        ) {
            startActivity(intent)
        }
    }
}
