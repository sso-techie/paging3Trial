package com.ssotory.paging3trial.gallery.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ssotory.paging3trial.R
import com.ssotory.paging3trial.gallery.data.ImageData

class GalleryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(imageData: ImageData) {
        itemView.findViewById<ImageView>(R.id.image_view).setImageURI(imageData.uri)
    }
}