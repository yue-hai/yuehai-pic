package com.yuehai.pic.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.yuehai.pic.R
import com.yuehai.pic.utils.AppInitializer

/**
 * 图片详情页的顶部操作栏
 */
class FragmentImageViewerTopInformation : Fragment() {
	
	/**
	 * onCreateView 是碎片的生命周期中的一种状态，在为碎片创建视图（加载布局）时调用
	 *
	 * LayoutInflater inflater：作用类似于 findViewById()
	 *      findViewById（）用来寻找 xml 布局下的具体的控件（Button、TextView等）
	 *      LayoutInflater inflater() 用来找 res/layout/ 下的 xml 布局文件
	 * ViewGroup container：表示容器，View 放在里面
	 * Bundle savedInstanceState：保存当前的状态，在活动的生命周期中，只要离开了可见阶段，活动很可能就会被进程终止，这种机制能保存当时的状态
	 */
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		
		// 加载 Fragment 布局
		val view = inflater.inflate(R.layout.fragment_image_viewer_top_information, container, false)
		
		// 获取根布局，设置上边距
		val topInformation = view.findViewById<LinearLayout>(R.id.fragment_image_viewer_top_information)
		val linearLayout = topInformation.layoutParams as FrameLayout.LayoutParams
		linearLayout.topMargin = AppInitializer().getNotificationBarHeight(context as Activity)
		view.layoutParams = linearLayout
		
		// 给返回按钮绑定点击事件
		view.findViewById<ImageButton>(R.id.top_information_exit).setOnClickListener { onClickListenerExit() }
		// 给详细信息按钮绑定点击事件
		view.findViewById<ImageButton>(R.id.top_information_details).setOnClickListener { onClickListenerDetails() }

		return view
	}
	
	/**
	 * 设置返回按钮，点击退出详情页，关闭该 activity
	 */
	private fun onClickListenerExit(){
		activity?.finish()
	}
	
	/**
	 * 点击详细信息显示详细信息弹窗
	 */
	private fun onClickListenerDetails(){
		// 获取详细信息弹窗控件
		val details = activity?.findViewById<LinearLayout>(R.id.image_viewer_details)
		// 判断图片详细信息弹窗是否显示，若是显示则隐藏，若是隐藏则显示
		details?.visibility = if (details?.visibility == View.GONE) View.VISIBLE else View.GONE
	}
	
}