package com.yuehai.pic.ui.activity.adapter.holder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.yuehai.pic.R

/**
 * ImageViewerAdapter 对应的 holder 对象
 */
class ImageViewerHolder(view: View) : RecyclerView.ViewHolder(view) {
	var imageView: ImageView
	init {
		imageView = view.findViewById(R.id.image_details_item)
	}
}