package com.yuehai.pic.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yuehai.pic.bean.global.Config.GLIDE_DISK_CACHE_STRATEGY
import com.yuehai.pic.bean.global.Config.SORT_METHOD
import com.yuehai.pic.bean.global.Config.VIEW_MODE
import kotlin.math.min


/**
 * 软件初始化工具类
 */
class AppInitializer {
	
	/**
	 * 初始化应用数据
	 * @param context Activity
	 */
	fun initializeApplicationData(context : Context){
		// 获取 SharedPreferences 对象
		val sharedPreferences = context.getSharedPreferences("yuehai-pic", Context.MODE_PRIVATE)
		
		// 设置缓存加载策略，默认为不使用磁盘缓存
		when(sharedPreferences.getInt("select_cache_loading_strategy", 1)){
			0 -> { GLIDE_DISK_CACHE_STRATEGY = DiskCacheStrategy.ALL }
			1 -> { GLIDE_DISK_CACHE_STRATEGY = DiskCacheStrategy.NONE }
			2 -> { GLIDE_DISK_CACHE_STRATEGY = DiskCacheStrategy.DATA }
			3 -> { GLIDE_DISK_CACHE_STRATEGY = DiskCacheStrategy.RESOURCE }
			4 -> { GLIDE_DISK_CACHE_STRATEGY = DiskCacheStrategy.AUTOMATIC }
		}
		
		// 设置排序方式
		when(sharedPreferences.getInt("select_sort_method", 0)){
			0 -> { SORT_METHOD = MediaStore.Files.FileColumns.DATE_ADDED + " DESC" }
			1 -> { SORT_METHOD = MediaStore.Files.FileColumns.DATE_ADDED + " ASC" }
			2 -> { SORT_METHOD = MediaStore.Files.FileColumns.TITLE + " DESC" }
			3 -> { SORT_METHOD = MediaStore.Files.FileColumns.TITLE + " ASC" }
		}
		
		// 设置视图模式
		VIEW_MODE = sharedPreferences.getInt("select_view_mode", 0)
		
	}
	
	/**
	 * 判断当前通知栏和导航栏是否显示
	 * @param context Activity
	 * @return true：正在显示；false：已经隐藏
	 */
	fun isSystemUIVisible(context : Context): Boolean {
		val decorView = (context as Activity).window.decorView
		val visibility = decorView.systemUiVisibility
		val flags = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
		
		// 判断通知栏和导航栏是否显示
		return visibility and flags == 0
	}
	
	/**
	 * 隐藏通知栏和导航栏，该行代码应在 setContentView() 方法之后调用
	 * @param context Activity
	 */
	fun hideStatusBar(context : Context){
		// 通过使用位运算符 or 来进行组合，将其赋值给 systemUiVisibility 属性，可以同时隐藏状态栏和导航栏
		(context as Activity).window.decorView.systemUiVisibility = (
				// 这个标志用于创建沉浸式体验，让应用程序的界面充满整个屏幕，隐藏系统栏。
				// 与其他标志不同的是，如果用户交互或者滑动从屏幕边缘处显示系统栏，这个标志会以半透明的方式显示系统栏，并在一段时间后自动隐藏。
				View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
						// 这个标志保持布局稳定，不管系统栏的可见性如何变化，保持布局不变。它通常与其他标志结合使用，确保布局不会在显示或隐藏系统栏时重新计算。
						or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						// 这个标志使布局扩展到底部导航栏的位置。它确保应用程序的内容不会被导航栏覆盖，而是布局会扩展到导航栏的底部
						or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						// 这个标志使布局扩展到顶部状态栏的位置。它确保应用程序的内容不会被状态栏覆盖，而是布局会扩展到状态栏的顶部
						or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						// 这个标志隐藏底部导航栏。当应用程序获取焦点时，导航栏会被隐藏。用户可以通过交互或者滑动来显示导航栏
						or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						// 这个标志隐藏顶部状态栏。当应用程序获取焦点时，状态栏会被隐藏。用户可以通过交互或者滑动来显示状态栏
						or View.SYSTEM_UI_FLAG_FULLSCREEN
						// 4.4 以后这个属性是用来实现“沉浸式”效果的
						or View.SYSTEM_UI_FLAG_IMMERSIVE
				)
	}
	
	/**
	 * 显示通知栏和导航栏，该行代码应在 setContentView() 方法之后调用
	 * @param context Activity
	 */
	fun showStatusBar(context : Context){
		(context as Activity).window.decorView.systemUiVisibility = 0
	}
	
	/**
	 * 获取状态栏高度
	 * 至于为什么 获取状态栏高度 没有判断状态栏是否隐藏，只判断了导航栏是否隐藏的原因：
	 * 当我需要状态栏高度和导航栏高度时，按我的业务逻辑，状态栏是肯定会显示的，
	 * 但是导航栏就不一定了，他取决于用户的系统有没有关闭导航栏
	 */
	fun getNotificationBarHeight(context: Context): Int {
		val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
		return if (resourceId > 0) {
			context.resources.getDimensionPixelSize(resourceId)
		} else {
			0
		}
	}
	
	/**
	 * 获取导航栏高度
	 */
	fun getNavigationBarHeight(context: Context): Int {
		/**
		 * 判断系统是否大于 android 10，大于则直接获取底部导航栏高度
		 * 小于则：
		 *      获取内容高度 + 通知栏高度 = 屏幕高度：导航栏已隐藏，返回 0
		 *      获取内容高度 + 通知栏高度 < 屏幕高度：导航栏未隐藏，返回导航栏高度
		 */
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			// 获取当前窗口的度量信息
			val windowMetrics = (context as Activity).windowManager.currentWindowMetrics
			// 获取系统栏插入物（通知栏、导航栏、软键盘）
			val insets = windowMetrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
			// 直接获取底部导航栏高度
			return insets.bottom
		}
		
		// 获取内容高度（不包括通知栏和导航栏）
		val displayMetrics = context.resources.displayMetrics
		val displayMetricsHeight = displayMetrics.heightPixels
		
		// 获取通知栏高度
		val notificationBarHeight = getNotificationBarHeight(context)
		
		// 获取屏幕高度（包括通知栏和导航栏）
		val defaultDisplay = DisplayMetrics()
		(context as Activity).windowManager.defaultDisplay.getRealMetrics(defaultDisplay)
		val defaultDisplayHeight = defaultDisplay.heightPixels
		
		/**
		 * 获取内容高度 + 通知栏高度 = 屏幕高度：导航栏已隐藏，返回 0
		 */
		if (displayMetricsHeight + notificationBarHeight == defaultDisplayHeight){
			return 0
		}
		
		/**
		 * 获取内容高度 + 通知栏高度 < 屏幕高度：导航栏未隐藏，返回导航栏高度
		 * 应该不会有 > 的情况，所以这里就不判断了，有了这种情况再加判断
		 */
		val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
		return if (resourceId > 0) {
			context.resources.getDimensionPixelSize(resourceId)
		} else {
			0
		}
	}
	
	/**
	 * 设置圆形的图片 ImageView
	 * @param originalBitmap Bitmap 对象
	 * @return 设置为圆形之后的 Bitmap 对象
	 */
	fun circularImageView(originalBitmap: Bitmap): Bitmap {
		// 1、创建一个与原始图片大小相同的空白 Bitmap 对象，用于绘制圆形图片
		val circularBitmap = Bitmap.createBitmap(
			originalBitmap.width,
			originalBitmap.height,
			Bitmap.Config.ARGB_8888
		)
		// 2、创建画笔对象
		val paint = Paint()
		
		/**
		 * 3、创建着色器对象，使用原始图片作为着色器
		 * 参数 1、bitmap: Bitmap 对象，作为着色器的源图片。
		 * 参数 2、tileX: 横向的 TileMode（平铺模式）。可选值有：
		 *      CLAMP：拉伸最后一个像素填充剩余区域。
		 *      REPEAT：重复平铺图片。
		 *      MIRROR：镜像平铺图片。
		 * 参数 3、tileY: 纵向的 TileMode（平铺模式）。可选值同上。
		 * 参数 4、matrix: 可选参数，用于对源图片进行变换的 Matrix。
		 */
		val shader: Shader = BitmapShader(originalBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
		// 将着色器设置给画笔
		paint.shader = shader
		// 将着色器设置给画笔
		paint.isAntiAlias = true
		// 获取图片资源的长、宽，并取长宽中较短的一边赋值给 shortSide
		val radiusWidth = originalBitmap.width / 2f
		val radiusHeight = originalBitmap.height / 2f
		val shortSide = min(radiusWidth, radiusHeight)
		/**
		 * 4、创建画布对象，并在其中绘制圆形图片
		 * 参数 1、圆心的 x 坐标：即圆心在 Canvas 上的横向位置
		 * 参数 2、圆心的 y 坐标：即圆心在 Canvas 上的纵向位置
		 * 参数 3、圆的半径：即圆的大小，从圆心到圆周上的任意一点的距离
		 * 参数 4、画笔 Paint：用于定义圆形的样式、颜色等属性的 Paint 对象
		 */
		Canvas(circularBitmap).drawCircle(radiusWidth, radiusHeight, shortSide, paint)
		
		return circularBitmap
	}
	
	/**
	 * 设置圆角矩形的文本框
	 * @param radius 圆角值
	 * @param backgroundColor 颜色资源
	 */
	fun roundedRectangleTextView(radius: Float, backgroundColor: Int): GradientDrawable{
		// 创建一个 GradientDrawable 对象
		val gradientDrawable = GradientDrawable()
		// 设置圆角半径
		gradientDrawable.cornerRadius = radius
		// 设置背景颜色
		gradientDrawable.setColor(backgroundColor)
		
		return gradientDrawable
	}
	
	
}