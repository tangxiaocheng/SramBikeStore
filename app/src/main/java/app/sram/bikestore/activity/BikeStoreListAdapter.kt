package app.sram.bikestore.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.sram.bikestore.data.BikeStoreItem
import app.sram.bikestore.databinding.BikeStoreListItemBinding
import app.sram.bikestore.di.ui.FragmentScope
import coil.load
import javax.inject.Inject
@FragmentScope
class BikeStoreListAdapter @Inject constructor() : RecyclerView.Adapter<BikeStoreListAdapter.ViewHolder>() {

    private val stateList: MutableList<BikeStoreItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BikeStoreListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun fresh() {
        stateList.clear()
        notifyDataSetChanged()
    }

    fun append(list: List<BikeStoreItem>) {
        val currentSize = stateList.size
        stateList.addAll(list)
        notifyItemRangeChanged(currentSize, list.size)
    }

    override fun getItemCount(): Int = stateList.size

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        bindData(holder, stateList[index], index + 1)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(
        holder: ViewHolder,
        bikeStoreItem: BikeStoreItem,
        position: Int
    ) {
        holder.storeDistTv.text = "${bikeStoreItem.formatDistance} :$position"
        holder.stateNameTv.text = bikeStoreItem.name
        holder.ratingBar.rating = bikeStoreItem.rating
        holder.ratingTv.text = bikeStoreItem.rating.toString()
        holder.totalRatingTv.text = "(${bikeStoreItem.userRatingsTotal})"
        holder.root.setOnClickListener {
            TODO("revoke the onClick event in the main fragment")
//            BikeStoreDetailFragment.newInstance(bikeStoreItem)
        }
        holder.storePhotoIv.load(bikeStoreItem.photoUrl)
    }

    inner class ViewHolder(binding: BikeStoreListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val stateNameTv: TextView = binding.storeNameTv
        val ratingBar: RatingBar = binding.storeRb
        val storePhotoIv: ImageView = binding.storePhotoIv
        val ratingTv: TextView = binding.ratingTv
        val storeDistTv: TextView = binding.storeDistTv
        val totalRatingTv: TextView = binding.totalRatingTv
        val root = binding.root
    }
}
