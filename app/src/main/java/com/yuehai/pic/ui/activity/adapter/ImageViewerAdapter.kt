package com.yuehai.pic.ui.activity.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yuehai.pic.R
import com.yuehai.pic.bean.global.Global
import com.yuehai.pic.ui.activity.adapter.holder.ImageViewerHolder
import com.yuehai.pic.utils.AppInitializer
import com.yuehai.pic.utils.GlideUtil

/**
 * 图片详情页的横向翻页视图适配器
 */
class ImageViewerAdapter(
	// 上下文环境
	private var context: Context
): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
	
	/**
	 * onCreateViewHolder() 用于创建新的 ViewHolder 对象，当 RecyclerView 需要新的 ViewHolder 时，会调用此方法。
	 * 该方法会创建并初始化 ViewHolder 及其关联的视图，但不会填充视图的内容，因为此时 ViewHolder 尚未绑定到具体数据。
	 * 该方法的返回值是一个 ViewHolder 对象。在该方法中，您需要创建一个新的 View 对象，并将其包装在一个新的 ViewHolder 对象中。
	 */
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val view = LayoutInflater.from(context).inflate(R.layout.activity_image_viewer_details_item, parent, false)
		return ImageViewerHolder(view)
	}
	
	/**
	 * onBindViewHolder() 用于将数据与 ViewHolder 对象关联起来。当 RecyclerView 需要新的数据绑定到 ViewHolder 时，会调用此方法。
	 * 该方法会提取适当的数据，并使用该数据填充 ViewHolder 的布局。
	 * 该方法的第一个参数是一个 ViewHolder 对象，第二个参数是该 ViewHolder 在列表中的位置。在该方法中，您需要使用适当的数据填充视图。
	 */
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		// 使用 Glide 加载图片
		GlideUtil().imageDetails(context, (holder as ImageViewerHolder).imageView, Global.imageDataList[position].id)
		
		// 获取 context 转换为 Activity 的对象
		val activity = context as Activity
		// 获取作为通知栏背景的控件对象
		val notificationBar = activity.findViewById<TextView>(R.id.represents_the_notification_bar)
		// 获取作为导航栏背景的控件对象
		val navigationBar = activity.findViewById<TextView>(R.id.represents_the_navigation_bar)
		// 获取顶部返回按钮和图片信息等的控件对象
		val topInformation = activity.findViewById<FragmentContainerView>(R.id.image_viewer_fragment_top_information)
		// 获取底部图片操作按钮的控件对象
		val bottomOperation = activity.findViewById<FragmentContainerView>(R.id.image_viewer_fragment_bottom_operation)
		
		// 给每个视图绑定点击事件
		holder.itemView.setOnClickListener {
			// 判断当前通知栏和导航栏是否显示，true：正在显示；false：已经隐藏
			if (AppInitializer().isSystemUIVisible(context)){
				// 调用方法，隐藏通知栏和导航栏
				AppInitializer().hideStatusBar(context)
				
				// 隐藏作为通知栏背景的控件对象、作为导航栏背景的控件对象、顶部返回按钮和图片信息、底部图片操作按钮
				notificationBar.visibility = View.GONE
				navigationBar.visibility = View.GONE
				topInformation.visibility = View.GONE
				bottomOperation.visibility = View.GONE
			}else{
				// 调用方法，显示通知栏和导航栏
				AppInitializer().showStatusBar(context)
				
				// 显示作为通知栏背景的控件对象、作为导航栏背景的控件对象、顶部返回按钮和图片信息、底部图片操作按钮
				notificationBar.visibility = View.VISIBLE
				navigationBar.visibility = View.VISIBLE
				topInformation.visibility = View.VISIBLE
				bottomOperation.visibility = View.VISIBLE
			}
			
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
		val imageView: ImageView = (holder as ImageViewerHolder).imageView
		Glide.with(context).clear(imageView)
	}
	
	/**
	 * getItemCount() 用于获取数据集中的元素数量。当 RecyclerView 需要知道列表中有多少项时，会调用此方法。
	 * 该方法的返回值是一个整数，表示数据集中的元素数量。
	 */
	override fun getItemCount(): Int {
		return Global.imageDataList.size
	}
}