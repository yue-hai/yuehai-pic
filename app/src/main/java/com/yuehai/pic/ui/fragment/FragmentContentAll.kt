package com.yuehai.pic.ui.fragment

import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.yuehai.pic.R
import com.yuehai.pic.ui.fragment.adapter.FragmentContentAllAdapter
import com.yuehai.pic.utils.PictureUtil


// 主体内容：全部
class FragmentContentAll(
	// 上下文环境
	private var context: Context,
	// 内容提供器
	private val contentResolver: ContentResolver
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
		
		// 获取控件对象
		val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_content_all_item_RecyclerView)
		
		/**
		 * 设置网格布局管理器，这个方法的作用是将RecyclerView的子项按照网格方式排列
		 *
		 * 第一个参数 context 表示上下文
		 * 第二个参数 4 表示每行的列数；如果是横向滚动，后面的数值表示的是几行，如果是竖向滚动，后面的数值表示的是几列
		 * 第三个参数 LinearLayoutManager.VERTICAL 表示布局方向为垂直方向
		 * 第四个参数 false 表示是否反转布局
		 */
		recyclerView.layoutManager = GridLayoutManager(context, 4, LinearLayoutManager.VERTICAL, false)
		
		// 设置适配器
		recyclerView.adapter = FragmentContentAllAdapter(context, PictureUtil().getImageAll(contentResolver))
		
		// 关闭动画
		(recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
		
		return view
	}
	
}