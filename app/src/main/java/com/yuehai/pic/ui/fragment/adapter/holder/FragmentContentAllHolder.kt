package com.yuehai.pic.ui.fragment.adapter.holder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.yuehai.pic.R

/**
 * FragmentContentAllAdapter 对应的 holder 对象
 */
class FragmentContentAllHolder(view: View) : RecyclerView.ViewHolder(view) {
	var imageView: ImageView
	init {
		imageView = view.findViewById(R.id.pic_item)
	}
}