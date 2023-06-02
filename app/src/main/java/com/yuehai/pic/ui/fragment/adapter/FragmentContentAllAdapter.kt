package com.yuehai.pic.ui.fragment.adapter

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yuehai.pic.R
import com.yuehai.pic.ui.fragment.adapter.holder.FragmentContentAllHolder


class FragmentContentAllAdapter(
	// 上下文环境
	private var context: Context,
	// 所需数据
	private var data: MutableList<Long>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	
	/**
	 * onCreateViewHolder() 用于创建新的 ViewHolder 对象，当 RecyclerView 需要新的 ViewHolder 时，会调用此方法。
	 * 该方法会创建并初始化 ViewHolder 及其关联的视图，但不会填充视图的内容，因为此时 ViewHolder 尚未绑定到具体数据。
	 * 该方法的返回值是一个 ViewHolder 对象。在该方法中，您需要创建一个新的 View 对象，并将其包装在一个新的 ViewHolder 对象中。
	 */
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val view = LayoutInflater.from(context).inflate(R.layout.fragment_content_pic_item, parent, false)
		return FragmentContentAllHolder(view)
	}
	
	/**
	 * onBindViewHolder() 用于将数据与 ViewHolder 对象关联起来。当 RecyclerView 需要新的数据绑定到 ViewHolder 时，会调用此方法。
	 * 该方法会提取适当的数据，并使用该数据填充 ViewHolder 的布局。
	 * 该方法的第一个参数是一个 ViewHolder 对象，第二个参数是该 ViewHolder 在列表中的位置。在该方法中，您需要使用适当的数据填充视图。
	 */
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		// 使用 Glide 加载图片
		Glide.with(context)
			// 图片的绘制方式，相对而言，asDrawable() 比 asBitmap() 要省
			.asDrawable()
			// 设置图片 uri
			.load(ContentUris.withAppendedId(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				data[position]
			))
			// 设置默认显示的图片
			.placeholder(androidx.appcompat.R.drawable.abc_tab_indicator_mtrl_alpha)
			// 跳过内存缓存
			.skipMemoryCache(true)
			// 全部使用磁盘缓存
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			// 裁剪图片大小
			.override(200, 200)
			// 设置视图
			.into((holder as FragmentContentAllHolder).imageView)
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
		return data.size
	}
	
}

