package com.dattilio.flicker.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dattilio.flicker.databinding.ItemImageGridBinding
import com.dattilio.flicker.search.model.Image

class SearchAdapter(private val onImageClicked: OnImageClickedListener) : RecyclerView.Adapter<ImageViewHolder>() {
    private val images: MutableList<Image> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val imageBinding = ItemImageGridBinding.inflate(inflater, parent, false)
        return ImageViewHolder(imageBinding, onImageClicked)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    fun update(newData: List<Image>) {
        images.clear()
        images.addAll(newData)
        notifyDataSetChanged()
    }
}

class ImageViewHolder(
    private val imageBinding: ItemImageGridBinding,
    private val onImageClicked: OnImageClickedListener
) : RecyclerView.ViewHolder(imageBinding.root) {
    fun bind(image: Image) {
        imageBinding.image = image
        imageBinding.imageClicked = onImageClicked
    }
}