package com.yuehai.pic.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore

/**
 * 图片工具类
 */
class PictureUtil {
	
	/**
	 * 查询所有图片
	 * @param contentResolver 内容提供器
	 * @param sortOrder 排序方式，默认按时间降序
	 */
	fun getImageAll(
		contentResolver: ContentResolver,
		sortOrder: String = MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
	): MutableList<Long>{
		
		val data: MutableList<Long> = mutableListOf()

		/**
		 * 通过 contentResolver.query() 方法可以查询 MediaStore 数据库，这个方法接收五个参数：
		 *
		 * 1：uri：内容提供者中文件的路径，有以下四种：
		 *      MediaStore.Files.getContentUri(“external”)：全部内容
		 *      MediaStore.Video.Media.EXTERNAL_CONTENT_URI：视频内容
		 *      MediaStore.Audio.Media.EXTERNAL_CONTENT_URI：音频内容
		 *      MediaStore.Images.Media.EXTERNAL_CONTENT_URI：图片内容
		 * 2：projection：需要查询出来的列，文件的 _id、文件的大小等
		 * 3：selection：查询的条件，比如 selection = "mime_type = ?";
		 * 4：selectionArgs：查询的条件的值，比如: selectionArgs = new String[]{"video/mp4"};
		 * 5：sortOrder：排序
		 *      比如按照添加日期降序排序：MediaStore.Files.FileColumns.DATE_ADDED + "DESC"
		 *      比如按照添加日期升序排序：MediaStore.Files.FileColumns.DATE_ADDED + "ASC"
		 */
		val cursor = contentResolver.query(
			MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
			arrayOf(MediaStore.Images.Media._ID),
			null,
			null,
			sortOrder
		)
		
		// 如果查询到了数据
		if (cursor!!.moveToFirst()) {
			// 遍历查询结果
			while (cursor.moveToNext()){
				// 获取图片的 ID
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