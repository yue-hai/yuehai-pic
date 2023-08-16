package com.yuehai.pic.bean

/**
 * 查询出的图片对象
 */
data class ImageData(
	/**
	 * 图片 id
	 */
	var id: Long,
	/**
	 * 文件的绝对路径
	 */
	var data: String,
	/**
	 * 标题名称
	 */
	var title: String,
	/**
	 * 文件名
	 */
	var fileName: String,
	/**
	 * 后缀名
	 */
	val suffixName: String,
	/**
	 * 文件大小
	 */
	val size: Long,
	/**
	 * 文件类型
	 */
	val type: String,
	/**
	 * 来自哪个包
	 */
	val bucketDisplayName: String,
	/**
	 * 添加日期
	 */
	val dateAdded: Long,
)