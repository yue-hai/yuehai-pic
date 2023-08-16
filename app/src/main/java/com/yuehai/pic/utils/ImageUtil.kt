package com.yuehai.pic.utils

import android.content.ContentResolver
import android.provider.MediaStore
import com.yuehai.pic.bean.ImageData
import com.yuehai.pic.bean.global.Config.SORT_METHOD
import com.yuehai.pic.bean.global.Global.IMAGE_DATA_LIST
import java.io.File

/**
 * 图片工具类
 */
class ImageUtil {
	
	/**
	 * 查询所有图片
	 * @param contentResolver 内容提供器
	 */
	fun getImageAll(contentResolver: ContentResolver){
		
		// 清空集合中的数据
		IMAGE_DATA_LIST.clear()
		
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
			arrayOf(
				MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA,
				MediaStore.Images.Media.TITLE,
				MediaStore.Images.Media.SIZE,
				MediaStore.Images.Media.MIME_TYPE,
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
				MediaStore.Images.Media.DATE_ADDED,
			),
			null,
			null,
			SORT_METHOD
		)
		
		// 如果查询到了数据
		if (cursor!!.moveToFirst()) {
			// 遍历查询结果
			while (cursor.moveToNext()){
				// 获取图片的 ID、绝对路径、名称、大小、类型、添加日期、来自哪个包
				val idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
				val dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
				val titleColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.TITLE)
				val sizeColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
				val typeColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)
				val bucketDisplayNameColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
				val dateAddedColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
				
				// 文件全路径
				val data = cursor.getString(dataColumnIndex)
				// 文件名和后缀名
				val fileAndSuffixName = data.substring(data.lastIndexOf(File.separator) + 1)
				
				// 将图片的 Uri 添加到 IMAGE_DATA_LIST 中
				IMAGE_DATA_LIST.add(ImageData(
					cursor.getLong(idColumnIndex),
					data,
					cursor.getString(titleColumnIndex),
					fileAndSuffixName.substring(0, fileAndSuffixName.lastIndexOf(".")),
					fileAndSuffixName.substring(fileAndSuffixName.lastIndexOf(".")),
					cursor.getLong(sizeColumnIndex),
					cursor.getString(typeColumnIndex),
					cursor.getString(bucketDisplayNameColumnIndex),
					cursor.getLong(dateAddedColumnIndex),
				))
			}
		}
		
		// 关闭 cursor
		cursor.close()
	}
	
	/**
	 * 根据路径获取 id
	 * @param contentResolver 内容提供器
	 * @param path 全路径
	 */
	fun getTheIdAccordingToThePath(contentResolver: ContentResolver, path: String): Long{
		// 需要查询出来的列，文件的 _id
		val projection = arrayOf(MediaStore.Images.Media._ID)
		// 查询的条件，路径
		val selection = "${MediaStore.Images.Media.DATA} = ?"
		// 查询的条件的值，路径
		val selectionArgs = arrayOf(path)

		// 查询
		val cursor = contentResolver.query(
			MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
			projection,
			selection,
			selectionArgs,
			null
		)
		
		// 获取查询到的 id
		var imageId: Long? = null
		if (cursor!!.moveToFirst()){
			val idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
			imageId = cursor.getLong(idColumnIndex)
		}
		
		// 关闭 cursor
		cursor.close()
		// 返回查询到的 id
		return imageId!!
	}
	
}