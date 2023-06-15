package com.yuehai.pic.ui.fragment.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yuehai.pic.R
import com.yuehai.pic.bean.global.Global.imageDataList
import com.yuehai.pic.ui.activity.ImageViewerActivity
import com.yuehai.pic.ui.fragment.adapter.holder.FragmentContentAllHolder
import com.yuehai.pic.utils.GlideUtil


class FragmentContentAllAdapter(
	// 上下文环境
	private var context: Context
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	
	/**
	 * onCreateViewHolder() 用于创建新的 ViewHolder 对象，当 RecyclerView 需要新的 ViewHolder 时，会调用此方法。
	 * 该方法会创建并初始化 ViewHolder 及其关联的视图，但不会填充视图的内容，因为此时 ViewHolder 尚未绑定到具体数据。
	 * 该方法的返回值是一个 ViewHolder 对象。在该方法中，您需要创建一个新的 View 对象，并将其包装在一个新的 ViewHolder 对象中。
	 */
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val view = LayoutInflater.from(context).inflate(R.layout.fragment_image_list_thumbnail_item, parent, false)
		return FragmentContentAllHolder(view)
	}
	
	/**
	 * onBindViewHolder() 用于将数据与 ViewHolder 对象关联起来。当 RecyclerView 需要新的数据绑定到 ViewHolder 时，会调用此方法。
	 * 该方法会提取适当的数据，并使用该数据填充 ViewHolder 的布局。
	 * 该方法的第一个参数是一个 ViewHolder 对象，第二个参数是该 ViewHolder 在列表中的位置。在该方法中，您需要使用适当的数据填充视图。
	 */
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		// 使用 Glide 加载图片
		GlideUtil().imageListThumbnail(context, (holder as FragmentContentAllHolder).imageView, imageDataList[position].id)

		// 给每个视图绑定点击事件
		holder.itemView.setOnClickListener {
			// 向下一个 Activity 发送消息，显式 Intent 传递：在 Intent 的构造函数中指定
			val intent = Intent(context, ImageViewerActivity().javaClass)
			// 通过 bundle 包装数据
			val bundle = Bundle()
			bundle.putInt("position", position)
			// 将 bundle 放入 intent 对象中
			intent.putExtras(bundle)
			// 跳转到跳转到自己定义的 activity，传递 intent 对象
			context.startActivity(intent)
		}
	}
	
	/**
	 * onViewRecycled() 用于回收 ViewHolder 对象。当 RecyclerView 需要回收 ViewHolder 时，会调用此方法。
	 * 该方法会释放 ViewHolder 的资源，以便在下一次使用时重新绑定数据。
	 * 该方法的参数是一个 ViewHolder 对象，表示需要回收的视图。
	 */
	override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
		super.onViewRecycled(holder)
		
		// ViewHolder 对象被回收时也销毁对应的 Glide 对象
		val imageView: ImageView = (holder as FragmentContentAllHolder).imageView
		Glide.with(context).clear(imageView)
	}
	
	/**
	 * getItemCount() 用于获取数据集中的元素数量。当 RecyclerView 需要知道列表中有多少项时，会调用此方法。
	 * 该方法的返回值是一个整数，表示数据集中的元素数量。
	 */
	override fun getItemCount(): Int {
		return imageDataList.size
	}
	
}

