package com.yuehai.pic.bean.global

import com.yuehai.pic.bean.ImageData

/**
 * 保存一些全局变量
 */
object Global {
	
	/**
	 * 触摸事件监听器 setOnTouchListener 使用
	 * 保存手指上一次被滑动监听时所在位置的垂直坐标，用于计算滑动距离
	 */
	var lastCoordinate: Float? = null
	
	/**
	 * 查询出的图片对象集合
	 */
	var imageDataList: MutableList<ImageData> = mutableListOf()
	
}