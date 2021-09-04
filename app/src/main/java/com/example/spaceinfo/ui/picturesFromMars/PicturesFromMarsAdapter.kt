package com.example.spaceinfo.ui.picturesFromMars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.spaceinfo.R
import com.example.spaceinfo.databinding.ItemPictureFormMarsBinding

class PicturesFromMarsAdapter() :
    RecyclerView.Adapter<PicturesFromMarsAdapter.PictureFromMarsViewHolder>() {
    private var pictures: List<String> = emptyList()

    fun setData(pictures: List<String>) {
        this.pictures = pictures
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PictureFromMarsViewHolder {
        return PictureFromMarsViewHolder(viewGroup)
    }

    override fun onBindViewHolder(viewHolder: PictureFromMarsViewHolder, position: Int) {
        viewHolder.bind(pictures[position])
    }

    override fun getItemCount() = pictures.size

    class PictureFromMarsViewHolder(viewGroup: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(viewGroup.context).inflate(R.layout.item_picture_form_mars, viewGroup, false)
    ) {
        private val binding: ItemPictureFormMarsBinding by viewBinding(ItemPictureFormMarsBinding::bind)

        fun bind(imagePath: String) {
            Glide.with(itemView.context).load(imagePath).into(binding.pictureFromMarsImageView)
        }
    }
}