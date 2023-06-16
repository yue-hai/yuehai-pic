package com.yuehai.pic.utils

import android.content.ContentUris
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.yuehai.pic.bean.global.Config

/**
 * Glide 处理工具类
 */
class GlideUtil {
	
	/**
	 * 加载图片列表，缩略图
	 */
	fun imageListThumbnail(context: Context, imageView: ImageView, imageId: Long){
		Glide.with(context)
			// 图片的绘制方式，相对而言，asDrawable() 比 asBitmap() 要省
			.asDrawable()
			// 设置图片 uri
			.load(ContentUris.withAppendedId(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				imageId
			))
			// 设置默认显示的图片，也可以是颜色资源
			.placeholder(ColorDrawable(Color.BLACK))
			// 跳过内存缓存
			.skipMemoryCache(true)
			// 不使用磁盘缓存
			.diskCacheStrategy(Config.GLIDE_DISK_CACHE_STRATEGY)
			// 裁剪图片大小
			.override(200, 200)
			// 将解码格式设置为 RGB_565
			.format(DecodeFormat.PREFER_RGB_565)
			// 设置视图
			.into(imageView)
	}
	
	/**
	 * 加载图片详情，原始图片
	 */
	fun imageDetails(context: Context, imageView: ImageView, imageId: Long){
		Glide.with(context)
			// 设置图片 uri
			.load(
				ContentUris.withAppendedId(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					imageId
				))
			// 设置默认显示的图片
			.placeholder(androidx.appcompat.R.drawable.abc_tab_indicator_mtrl_alpha)
			// 跳过内存缓存
			.skipMemoryCache(true)
			// 不使用磁盘缓存
			.diskCacheStrategy(Config.GLIDE_DISK_CACHE_STRATEGY)
			// 设置视图
			.into(imageView)
	}
	
}