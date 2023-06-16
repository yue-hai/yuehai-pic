package com.yuehai.pic.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.yuehai.pic.R

class FragmentToolBar: Fragment() {
	
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
		val view = inflater.inflate(R.layout.fragment_tool_bar, container, false)
		
		// 给菜单按钮添加点击事件
		view.findViewById<ImageButton>(R.id.tool_bar_settings_menu).setOnClickListener { onClickListenerSettingsMenu() }
		// 给排序按钮添加点击事件
		view.findViewById<ImageButton>(R.id.tool_bar_sort).setOnClickListener { onClickListenerSort() }
		// 给搜索按钮添加点击事件
		view.findViewById<ImageButton>(R.id.tool_bar_search).setOnClickListener { onClickListenerSearch() }
		// 给开始按钮添加点击事件
		view.findViewById<ImageButton>(R.id.tool_bar_start).setOnClickListener { onClickListenerStart() }
		
		return view
	}
	
	// 菜单按钮，点击打开侧边栏
	private fun onClickListenerSettingsMenu(){
		// 获取 DrawerLayout 整体布局
		val drawerLayout = activity?.findViewById<DrawerLayout>(R.id.drawer_menu)
		// 打开侧边栏
		drawerLayout?.openDrawer(GravityCompat.START)
	}
	
	// 排序按钮
	private fun onClickListenerSort(){
		Log.i("工具栏", "排序按钮")
	}
	
	// 搜索按钮
	private fun onClickListenerSearch(){
		Log.i("工具栏", "搜索按钮")
	}
	
	// 开始按钮
	private fun onClickListenerStart(){
		Log.i("工具栏", "开始按钮")
	}
	
	
}