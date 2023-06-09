package com.yuehai.pic.utils

import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.fragment.app.FragmentContainerView
import com.yuehai.pic.bean.global.Global.lastCoordinate

/**
 * 监视器调用的方法的工具类
 */
class ListenerUtil {
	
	/**
	 * 对顶部操作栏进行操作的触摸事件监听器调用的方法
	 * @param event 点击事件的对象
	 * @param toolBarView 所要修改的视图对象
	 *
	 * @return 此处因为要监听点击事件（FragmentContentAllAdapter 中），所以按下时要返回 false
	 * 又因为要监听点击事件的原因，按下时的监听不会触发，所以逻辑要写在滑动的监听中
	 */
	fun homeToolBarTouchListener(event: MotionEvent, toolBarView: FragmentContainerView) : Boolean{
		// 判断滑动事件种类
		when(event.action){
			// 手指按下时的处理逻辑
			MotionEvent.ACTION_DOWN -> {
			
			}
			// 手指移动时的处理逻辑
			MotionEvent.ACTION_MOVE -> {
				/**
				 * 判断上次的位置是否为 null
				 * 为 null 表示刚按下，第一次滑动，则将本次手指的坐标赋值给 coordinatesPressed，作为按下时的坐标使用
				 * 不为 null 表示不是刚按下，正在滑动中，则进行逻辑操作
				 */
				if (lastCoordinate == null){
					lastCoordinate = event.rawY
					return false
				}
				
				/**
				 * 本次的坐标 - 上次的坐标 = 赋值给移动的距离 + 上边距 = 滑动后上边距应该赋予的值
				 */
				val topMargin = (event.rawY - lastCoordinate!!) + toolBarView.marginTop
				
				/**
				 * android 的 view 中有 setPadding，但是没有直接的 setMargin 方法
				 * 如果要在代码中设置 Margin 可以通过设置 view 里面的 LayoutParams 设置
				 * 而这个 LayoutParams 是根据该 view 在不同的 GroupView 而不同的
				 *
				 * 1、获取所要修改 Margin 的 view 对象的 LayoutParams 对象
				 * 2、给 LayoutParams 对象的 topMargin（或其他属性）属性赋值
				 * 3、将 LayoutParams 对象赋值给所要修改 Margin 的 view 对象的 layoutParams 属性
				 */
				val layoutParams = toolBarView.layoutParams as ViewGroup.MarginLayoutParams
				
				/**
				 * 单独判断上边界和下边界，边界为 (0 - toolBarView.height) ~ 0
				 * topMargin < (0 - toolBarView.height)，表示已经完全隐藏，此时不应继续往上移动，赋值为 (0 - toolBarView.height)
				 * topMargin > 0，表示已经完全显示，此时不应继续往下移动，，赋值为 0
				 * 不属于两者，则为 (0 - toolBarView.height) ~ 0，此时正常进行赋值操作
				 */
				if (topMargin < (0 - toolBarView.height)){
					layoutParams.topMargin = (0 - toolBarView.height)
					toolBarView.layoutParams = layoutParams
				}else if (topMargin > 0){
					layoutParams.topMargin = 0
					toolBarView.layoutParams = layoutParams
				}else{
					layoutParams.topMargin = topMargin.toInt()
					toolBarView.layoutParams = layoutParams
				}
				
				// 上面的逻辑处理完毕之后，最后将本次手指的坐标赋值给 lastCoordinate，给下一次的滑动事件使用
				lastCoordinate = event.rawY
			}
			// 手指抬起时的处理逻辑
			MotionEvent.ACTION_UP -> {
				// 手指抬起时将 lastCoordinate 设置为 null，以便第一次按下滑动时的判断
				lastCoordinate = null
			}
		}
		
		return false
		
	}
	
}