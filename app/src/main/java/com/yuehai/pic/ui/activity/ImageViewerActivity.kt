package com.yuehai.pic.ui.activity

import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.yuehai.pic.R
import com.yuehai.pic.ui.activity.adapter.ImageViewerAdapter
import com.yuehai.pic.utils.AppInitializer

/**
 * 图片详情页 Activity
 */
class ImageViewerActivity: AppCompatActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// 设置内容视图；当前的组件显示哪个视图（窗口）；R 就是 res 包
		setContentView(R.layout.activity_image_viewer)
		/**
		 * 应用 activity 跳转动画
		 * 参数 1：要进入的的 activity 的进入动画，为 0 则为没有任何动画效果
		 * 参数 2：当前的 activity 的退出动画，为 0 则为没有任何动画效果
		 */
		overridePendingTransition(R.anim.image_details_enter, 0)
		
		/**
		 * 允许让状态栏和导航栏区域也显示本软件的内容，同时会让状态栏和导航栏变透明，无法改变其颜色
		 * WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS 标志位。该标志位允许布局延伸到屏幕边界，包括状态栏和导航栏区域
		 */
		window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
		
		// 通过 intent 获取上个 Activity 发送的消息
		val position = intent.extras!!.getInt("position")
		
		// 获取控件对象
		val recyclerView = findViewById<RecyclerView>(R.id.image_viewer_RecyclerView)
		/**
		 * 设置线性布局管理器
		 *
		 * 第一个参数 context 表示上下文
		 * 第二个参数 LinearLayoutManager.HORIZONTAL 表示布局方向为水平方向
		 * 第三个参数 false 表示是否反转布局
		 */
		recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
		// 设置适配器；调用方法获取全部图片信息
		recyclerView.adapter = ImageViewerAdapter(this)
		// 关闭更改动画
		(recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
		// 指定 RecyclerView 中显示的第几个子项
		recyclerView.scrollToPosition(position)
		// 使 RecyclerView 自动贴合页面，即在滚动到边缘时自动停止滚动
		val snapHelper = PagerSnapHelper()
		snapHelper.attachToRecyclerView(recyclerView)
		
		// 获取通知栏的高度
		val notificationBarHeight = AppInitializer().getNotificationBarHeight(this)
		// 获取导航栏的高度
		val navigationBarHeight = AppInitializer().getNavigationBarHeight(this)
		// 作为通知栏的背景，给他的高度赋值为通知栏的高度
		findViewById<TextView>(R.id.represents_the_notification_bar).layoutParams.height = notificationBarHeight
		// 作为导航栏的背景，给他的高度赋值为导航栏的高度
		findViewById<TextView>(R.id.represents_the_navigation_bar).layoutParams.height = navigationBarHeight
		// 给顶部返回按钮和图片信息控件的 topMargin 赋值为通知栏的高度
		val topInformation = findViewById<FragmentContainerView>(R.id.image_viewer_fragment_top_information)
		val topInformationLayoutParams = topInformation.layoutParams as ConstraintLayout.LayoutParams
		topInformationLayoutParams.topMargin = notificationBarHeight
		topInformation.layoutParams = topInformationLayoutParams
		// 给底部图片操作按钮控件的 bottomMargin 赋值为导航栏的高度
		val bottomOperation = findViewById<FragmentContainerView>(R.id.image_viewer_fragment_bottom_operation)
		val bottomOperationBarLayoutParams = bottomOperation.layoutParams as ConstraintLayout.LayoutParams
		bottomOperationBarLayoutParams.bottomMargin = navigationBarHeight
		bottomOperation.layoutParams = bottomOperationBarLayoutParams
	}
	
	/**
	 * 当用户按下 back 键时，默认行为是关闭当前的 Activity，调用此方法
	 */
	override fun finish() {
		super.finish()
		/**
		 * 应用 activity 跳转动画
		 * 参数 1：要进入的的 activity 的进入动画，为 0 则为没有任何动画效果
		 * 参数 2：当前的 activity 的退出动画，为 0 则为没有任何动画效果
		 */
		overridePendingTransition(0, R.anim.image_details_exit)
	}
	
}