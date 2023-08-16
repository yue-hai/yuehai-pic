package com.yuehai.pic.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.yuehai.pic.R
import com.yuehai.pic.bean.global.Global.LAST_COORDINATE_Y
import com.yuehai.pic.ui.fragment.adapter.FragmentContentAllAdapter


// 主体内容：全部
class FragmentContentAll: Fragment() {
	
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
		 * 设置网格布局管理器，这个方法的作用是将 RecyclerView 的子项按照网格方式排列
		 *
		 * 第一个参数 context 表示上下文
		 * 第二个参数 4 表示每行的列数；如果是横向滚动，后面的数值表示的是几行，如果是竖向滚动，后面的数值表示的是几列
		 * 第三个参数 LinearLayoutManager.VERTICAL 表示布局方向为垂直方向
		 * 第四个参数 false 表示是否反转布局
		 */
		recyclerView.layoutManager = GridLayoutManager(context, 4, LinearLayoutManager.VERTICAL, false)
		
		// 设置适配器；调用方法获取全部图片信息
		recyclerView.adapter = FragmentContentAllAdapter(requireContext())
		
		// 关闭更改动画
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
			return@setOnTouchListener toolBarTouchListener(event, activity?.findViewById(R.id.home_fragment_tool_bar))
		}
		
		return view
	}
	
	/**
	 * 对顶部操作栏进行操作的触摸事件监听器调用的方法
	 * @param event 点击事件的对象
	 * @param toolBarView 所要修改的视图对象
	 *
	 * @return 此处因为要监听点击事件（FragmentContentAllAdapter 中），所以按下时要返回 false
	 * 又因为要监听点击事件的原因，按下时的监听不会触发，所以逻辑要写在滑动的监听中
	 */
	private fun toolBarTouchListener(event: MotionEvent, toolBarView: FragmentContainerView?): Boolean{
		// 判断滑动事件种类
		when(event.action){
			/**
			 * 手指移动时的处理逻辑
			 */
			MotionEvent.ACTION_MOVE -> {
				/**
				 * 判断上次的位置是否为 null
				 * 为 null 表示刚按下，第一次滑动，则将本次手指的坐标赋值给 LAST_COORDINATE_Y，作为按下时的坐标使用
				 * 不为 null 表示不是刚按下，正在滑动中，则进行逻辑操作
				 */
				if (LAST_COORDINATE_Y == null){
					LAST_COORDINATE_Y = event.rawY
					return false
				}
				
				/**
				 * 本次的坐标 - 上次的坐标 = 赋值给移动的距离 + 上边距 = 滑动后上边距应该赋予的值
				 */
				val topMargin = (event.rawY - LAST_COORDINATE_Y!!) + toolBarView!!.marginTop
				
				/**
				 * android 的 view 中有 setPadding，但是没有直接的 setMargin 方法
				 * 如果要在代码中设置 Margin 可以通过设置 view 里面的 LayoutParams 设置
				 * 而这个 LayoutParams 是根据该 view 在不同的 GroupView 而不同的，具体取决于父布局的类型和需要修改的具体布局参数。
				 * 如果父布局是 ConstraintLayout，并且需要修改的布局参数是 ConstraintLayout.LayoutParams 中定义的特定属性（例如 `topMargin`）
				 *      那么可以使用 fragmentContainerView.layoutParams as ConstraintLayout.LayoutParams
				 * 如果父布局是任何类型的 ViewGroup，并且只需要修改通用的布局参数（例如 `topMargin`、`leftMargin` 等），
				 *      那么可以使用 fragmentContainerView.layoutParams as ViewGroup.MarginLayoutParams。
				 * MarginLayoutParams 是 ViewGroup 的一个子类，它包含了常用的边距属性。
				 * 所以，选择使用哪种写法取决于具体情况和需求。如果需要修改特定布局类型的特定属性，选择对应的布局参数类型；如果只需要修改通用属性，选择通用的 MarginLayoutParams
				 *
				 * 1、获取所要修改 Margin 的 view 对象的 LayoutParams 对象
				 * 2、给 LayoutParams 对象的 topMargin（或其他属性）属性赋值
				 * 3、将 LayoutParams 对象赋值给所要修改 Margin 的 view 对象的 layoutParams 属性
				 */
				val layoutParams = toolBarView.layoutParams as ViewGroup.MarginLayoutParams
				
				/**
				 * 单独判断上边界和下边界，边界为 -toolBarView.height ~ 0
				 * topMargin < -toolBarView.height，表示已经完全隐藏，此时不应继续往上移动，赋值为 -toolBarView.height
				 * topMargin > 0，表示已经完全显示，此时不应继续往下移动，，赋值为 0
				 * 不属于两者，则为 -toolBarView.height ~ 0，此时正常进行赋值操作
				 */
				if (topMargin < -toolBarView.height){
					layoutParams.topMargin = -toolBarView.height
					toolBarView.layoutParams = layoutParams
				}else if (topMargin > 0){
					layoutParams.topMargin = 0
					toolBarView.layoutParams = layoutParams
				}else{
					layoutParams.topMargin = topMargin.toInt()
					toolBarView.layoutParams = layoutParams
				}
				
				// 上面的逻辑处理完毕之后，最后将本次手指的坐标赋值给 LAST_COORDINATE_Y，给下一次的滑动事件使用
				LAST_COORDINATE_Y = event.rawY
			}
			/**
			 * 手指抬起时的处理逻辑
			 */
			MotionEvent.ACTION_UP -> {
				// 手指抬起时将 LAST_COORDINATE_Y 设置为 null，以便第一次按下滑动时的判断
				LAST_COORDINATE_Y = null
			}
		}
		
		return false
	}
	
	/**
	 * 刷新当前显示的区域
	 */
	fun refreshDisplayArea(){
		// 获取 RecyclerView 控件对象
		val recyclerView = view?.findViewById<RecyclerView>(R.id.fragment_content_all_item_RecyclerView)
		val layoutManager = recyclerView?.layoutManager as GridLayoutManager
		// 获取第一个可见项的索引
		val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
		// 获取最后一个可见项的索引
		val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
		/**
		 * 刷新当前所有可见项；未显示的位置，等用户滑到的时候就已经改变了
		 * 参数 1：要刷新的元素的首个索引
		 * 参数 2：要刷新的元素的个数
		 */
		recyclerView.adapter?.notifyItemRangeChanged(firstVisibleItemPosition, lastVisibleItemPosition - firstVisibleItemPosition + 1)
	}
	
}