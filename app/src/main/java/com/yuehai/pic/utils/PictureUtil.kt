package com.yuehai.pic.utils

import android.content.ContentResolver
import android.provider.MediaStore
import android.util.Log
import com.yuehai.pic.bean.global.PictureAll

class PictureUtil {
	
	fun getImageAll(contentResolver: ContentResolver){
		
		// 查询所有图片的 Uri
		val cursor = contentResolver.query(
			MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
			null,
			null,
			null,
			null
		)
		
		var num = 0
		
		// 如果查询到了数据
		if (cursor != null && cursor.moveToFirst()) {
			do {
				// 获取图片的 ID 和文件名等信息
				val idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
				val id = cursor.getLong(idColumnIndex)
				//val nameColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
				//val name = cursor.getString(nameColumnIndex)
				
				num++
				Log.i("月海 索引", num.toString())
				// Log.i("月海 cursor", cursor.)
				
				// 将图片的 Uri 添加到 imageURIList 中
				PictureAll.picAllList.add(id)
				
				if (num > 3500){
					return
				}
				
			} while (cursor.moveToNext())
		}
		
		cursor?.close()
		
	}
	
}