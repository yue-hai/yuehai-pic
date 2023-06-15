package com.yuehai.pic.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowInsets
import android.view.WindowManager


/**
 * 软件初始化工具类
 */
class AppInitializer {
	
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
	
	
	
	
	
}