package com.yuehai.pic.ui.fragment

import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.yuehai.pic.R
import com.yuehai.pic.ui.activity.SettingsActivity
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

		// 调用方法，判断是否是深色模式，是的话改变控件背景颜色、字体颜色、状态选择器颜色
		darkModeChangeControl(view)
		
		// 给头像控件设置资源，调用方法，并传入一个 Bitmap 对象
		view.findViewById<ImageView>(R.id.sidebar_menu_profile_photo).setImageBitmap(AppInitializer().circularImageView(BitmapFactory.decodeResource(resources, R.drawable.software_icon)))
		
		// 调用方法，给用户名控件文本框设置圆角矩形和背景色
		view.findViewById<TextView>(R.id.sidebar_menu_user_name).background = AppInitializer().roundedRectangleTextView(20f, getColor(requireContext(), R.color.yuehai_light_blue_slightly_dark))
	
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
		// 跳转到设置页面
		context?.startActivity(Intent(context, SettingsActivity().javaClass))
	}
	
	/**
	 * 关闭按钮，点击关闭软件
	 */
	private fun onClickListenerExit(){
		activity?.finish()
	}
	
	/**
	 * 判断是否是深色模式，是的话改变控件背景颜色、字体颜色、状态选择器颜色
	 */
	private fun darkModeChangeControl(view: View){
		// 判断是否是深色模式
		if (( requireContext().resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_YES) ) !=0){
			// 修改菜单列表的背景颜色
			view.findViewById<LinearLayout>(R.id.sidebar_menu_control_list).setBackgroundColor(Color.BLACK)
			// 修改设置按钮的字体颜色和状态选择器
			with(view.findViewById<TextView>(R.id.sidebar_menu_settings)){
				setTextColor(Color.WHITE)
				setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			}
			// 修改退出按钮的字体颜色和状态选择器
			with(view.findViewById<TextView>(R.id.sidebar_menu_exit)){
				setTextColor(Color.WHITE)
				setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			}
		}
	}
	
}