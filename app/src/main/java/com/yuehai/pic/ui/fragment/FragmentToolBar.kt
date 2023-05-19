package com.yuehai.pic.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
		
		// 给 Fragment 的按钮添加事件
		view.findViewById<ImageButton>(R.id.tool_bar_settings_menu).setOnClickListener { onClickListenerSettingsMenu() }
		view.findViewById<ImageButton>(R.id.tool_bar_sort).setOnClickListener { onClickListenerSort() }
		view.findViewById<ImageButton>(R.id.tool_bar_search).setOnClickListener { onClickListenerSearch() }
		view.findViewById<ImageButton>(R.id.tool_bar_start).setOnClickListener { onClickListenerStart() }
		
		return view
	}
	
	// 设置菜单
	private fun onClickListenerSettingsMenu(){
		Log.i("工具栏", "设置菜单")
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