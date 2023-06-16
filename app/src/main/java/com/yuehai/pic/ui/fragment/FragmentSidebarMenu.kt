package com.yuehai.pic.ui.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.yuehai.pic.R
import com.yuehai.pic.utils.AppInitializer


class FragmentSidebarMenu: Fragment() {
	
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
		val view = inflater.inflate(R.layout.sidebar_menu, container, false)
		
		// 获取头像控件
		val profilePhoto = view.findViewById<ImageView>(R.id.sidebar_menu_profile_photo)
		// 给控件设置资源，调用方法，并传入一个 Bitmap 对象
		profilePhoto.setImageBitmap(AppInitializer().circularImageView(BitmapFactory.decodeResource(resources, R.drawable.software_icon)))
		
		// 获取用户名控件
		val userName = view.findViewById<TextView>(R.id.sidebar_menu_user_name)
		// 调用方法，给文本框设置圆角矩形
		userName.background = AppInitializer().roundedRectangleTextView(20f, ContextCompat.getColor(requireContext(), R.color.yuehai_white_with_dark_blue))
	
		// 给设置按钮添加点击事件
		view.findViewById<TextView>(R.id.sidebar_menu_settings).setOnClickListener { onClickListenerSettings() }
		// 给退出按钮添加点击事件
		view.findViewById<TextView>(R.id.sidebar_menu_exit).setOnClickListener { onClickListenerExit() }
		
		return view
	}
	
	/**
	 * 设置按钮，点击进入设置页面
	 */
	private fun onClickListenerSettings(){
		Log.i("月海", "设置")
	}
	
	/**
	 * 关闭按钮，点击关闭软件
	 */
	private fun onClickListenerExit(){
		activity?.finish()
	}
	
}