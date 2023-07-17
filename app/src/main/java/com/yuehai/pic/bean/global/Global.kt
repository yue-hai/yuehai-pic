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
	var LAST_COORDINATE_Y: Float? = null
	
	/**
	 * 查询出的图片对象集合
	 */
	val IMAGE_DATA_LIST: MutableList<ImageData> = mutableListOf()
	
	/**
	 * 图片初始位置
	 */
	const val INITIAL_POSITION: Float = 0.0f
	
	/**
	 * 图片原始缩放尺寸
	 */
	const val ORIGINAL_SIZE_IMAGE: Float = 1.0f
	/**
	 * 图片放大到 2 倍
	 */
	const val DOUBLE_SIZE_IMAGE: Float = 2.0f
	/**
	 * 图片放大到 4 倍
	 */
	const val FOURFOLD_SIZE_IMAGE: Float = 4.0f

	/**
	 * ViewPropertyAnimator 对象的动画持续时间 400 ms
	 */
	const val ANIMATION_DURATION_FOUR_HUNDRED_MS: Long = 400
	
}