package com.yuehai.pic.ui.fragment

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.yuehai.pic.HomeActivity
import com.yuehai.pic.R
import com.yuehai.pic.ui.fragment.adapter.FragmentContentAllAdapter
import com.yuehai.pic.utils.ListenerUtil
import com.yuehai.pic.utils.PictureUtil


// 主体内容：全部
class FragmentContentAll(
	// 上下文环境
	private var context: Context,
	// 内容提供器
	private val contentResolver: ContentResolver,
	// HomeActivity 实例，用于获取 HomeActivity 中的其他控件
	private val homeActivity: HomeActivity
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
	@SuppressLint("ClickableViewAccessibility")
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
		
		// 设置适配器；调用方法获取全部图片信息
		recyclerView.adapter = FragmentContentAllAdapter(context, PictureUtil().getImageAll(contentResolver))
		
		// 关闭动画
		(recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
		
		/**
		 * 设置触摸事件监听器
		 *
		 * 值得注意的是，可能会有警告：Custom view `RecyclerView` has setOnTouchListener called on it but does not override performClick
		 * 说明：当添加了一些点击操作，例如像 setOnClickListener 这样的，它会调用 performClick才可以完成操作，
		 * 但重写了 onTouch，就有可能把 performClick 给屏蔽了，这样这些点击操作就没办法完成了，所以就会有了这个警告
		 */
		recyclerView.setOnTouchListener { _, event ->
			/**
			 * 调用方法，实现逻辑
			 * 返回值：true 来表示已消费了触摸事件，如果返回 false，则表示未消费触摸事件，可能会继续传递给其他触摸监听器或视图。
			 *  1、setOnTouchListener 单独使用的时候返回值需要设置为 true
			 *      这样才能保证 MotionEvent.ACTION_UP 的时候能获取相应的监听，而非一次监听（即每次只有一个按下的事件能被监听到）。
			 *  2、当 setOnTouchListener 和 setOnClickListener 同时使用时，onTouch 的返回值要设为 false
			 *      这样既可以保证按下，然后再抬起的时候可以被监听，并且点击事件也会被监听。
			 */
			return@setOnTouchListener ListenerUtil().homeToolBarTouchListener(event, homeActivity.findViewById<FragmentContainerView>(R.id.home_fragment_tool_bar))
		}
		
		return view
	}
	
	
}