package com.yuehai.pic.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.yuehai.pic.R
import com.yuehai.pic.ui.fragment.adapter.FragmentContentAllAdapter


// 主体内容：全部
class FragmentContentAll(
	// 所需数据
	private var data: MutableList<Long>
): Fragment()  {
	
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
		val view = inflater.inflate(R.layout.fragment_content_all, container, false)
		
		// 获取网格控件对象
		val gridView = view.findViewById<GridView>(R.id.fragment_content_all_item_GridView)
		// 声明一个适配器
		val starAdapter = FragmentContentAllAdapter(requireActivity().applicationContext, data)
		// 传入适配器实例
		gridView.adapter = starAdapter
		
		return view
	}
	
}