package com.yuehai.pic.ui.fragment.adapter

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yuehai.pic.R


class FragmentContentAllAdapter(
	// 上下文环境
	private var context: Context,
	// 所需数据
	private var data: MutableList<Long>
): ArrayAdapter<Long>(context, R.layout.fragment_content_all_item, data) {
	
	/**
	 * getView : 获取该条目要显示的界面
	 *
	 * 参数 1：表示当前索引
	 * 参数 2：缓存区
	 *      这个 convertView 就是最关键的部分
	 *      原理上讲，当 ListView 滑动的过程中，会有 item 被滑出屏幕，而不再被使用，
	 *      这时候 Android 会回收这个条目的 view，这个 view 也就是这里的 convertView
	 *      也就是为了不浪费内存重新调用第一个地址，就是被划上去的那个 View
	 *      如果不使用 convertView，当 item1 被移除屏幕的时候，会重新 new 一个 View 给新显示的 item_new
	 *      而如果使用了 convertView，我们可以复用它 这样就省去了 new View 的大量开销
	 * 参数 3：每个 ItemView 里面的容器  返回的 View 直接添加到容器中来
	 *
	 * 返回值：就是每个 ItemView 要显示的内容
	 */
	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		/**
		 * 判断 convertView 是否为空，不为空则返回自己，为空则生成视图对象并返回
		 * convertView 不能重新赋值，创建一个局部变量来保存返回的 View
		 */
		val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.fragment_content_all_item, null)
		
		val imageView = view as ImageView
		
		Log.i("月海", "FragmentContentAllAdapter $position")
		
		// 构造出图片的 Uri
		
		// 使用 Glide 加载图片
		Glide
			.with(context)
			.load(ContentUris.withAppendedId(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				data[position]
			))
			.into(imageView)
		
		// 给控件设置图片数据
		// imageView.setImageBitmap(data[position])
		// 设置标识符，以便复用
		// imageView.tag = position
		
		// 返回 view 对象
        return view
	}
}

