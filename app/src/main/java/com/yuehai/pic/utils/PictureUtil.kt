package com.yuehai.pic.utils

import android.content.ContentResolver
import android.provider.MediaStore

/**
 * 图片工具类
 */
class PictureUtil {
	
	/**
	 * 查询所有图片
	 */
	fun getImageAll(contentResolver: ContentResolver): MutableList<Long>{
		
		var data: MutableList<Long> = mutableListOf()
		
		// 只查询图片 url 和图片所在的目录
		// val projection = arrayOf(
		// 	MediaStore.Images.ImageColumns.DATA,
		// 	MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
		// )
		// // 图片的时间倒叙排列，图片的创建、修改会影响其所在目录的排序，排序按时间倒叙排列
		// val sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC "
		
		// // 查询所有图片
		// val cursor = contentResolver.query(
		// 	MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
		// 	projection,
		// 	null,
		// 	null,
		// 	sortOrder
		// )
		
		// 查询所有图片
		val cursor = contentResolver.query(
			MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
			null,
			null,
			null,
			null
		)
		
		// 如果查询到了数据
		if (cursor!!.moveToFirst()) {
			// 遍历查询结果
			while (cursor.moveToNext()){
				// 获取图片的 ID 和文件名等信息
				val idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
				val id = cursor.getLong(idColumnIndex)
				
				// 将图片的 Uri 添加到 imageURIList 中
				data.add(id)
			}
		}

		cursor.close()
		return data
	}
	
}