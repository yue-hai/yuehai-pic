package com.yuehai.pic.bean.global

/**
 * 保存一些全局变量
 */
object Global {
	
	/**
	 * 触摸事件监听器 setOnTouchListener 使用
	 * 保存手指上一次被滑动监听时所在位置的垂直坐标，用于计算滑动距离
	 */
	var lastCoordinate: Float? = null
	
}