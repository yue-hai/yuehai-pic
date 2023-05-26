package com.yuehai.pic.bean.global

/**
 * 单例类，主体内容：全部
 */
object PictureAll {
	// 当前索引
	var currentIndex: Int = 0
	
	// 用于保存全部视图中的图片数据
	var picAllList: MutableList<Long> = mutableListOf()
}