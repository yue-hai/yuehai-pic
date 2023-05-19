package com.yuehai.pic.ui.fragment.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.yuehai.pic.R

// class ImageAdapter(private val context: Context): RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
//
// 	private var images = emptyList<Uri>()
//
// 	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
// 		val imageView: ImageView = itemView.findViewById(R.id.imageView)
// 	}
//
// 	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
// 		// 加载item_image布局文件
// 		val view = LayoutInflater.from(parent.context)
// 			.inflate(R.layout.item_image, parent, false)
// 		return ViewHolder(view)
// 	}
//
// 	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
// 		// 使用Glide库加载图片
// 		Glide.with(context).load(images[position]).into(holder.imageView)
// 	}
//
// 	override fun getItemCount(): Int = images.size
//
// 	fun setImages(images: List<Uri>) {
// 		// 设置图片列表
// 		this.images = images
// 		notifyDataSetChanged()
// 	}
// }