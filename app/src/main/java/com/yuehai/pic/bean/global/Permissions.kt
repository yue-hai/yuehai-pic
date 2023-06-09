package com.yuehai.pic.bean.global

import android.Manifest

/**
 * 权限数组单例类
 */
object Permissions {
	
	/**
	 * 存储卡读写权限
	 */
	val PERMISSIONS_STORAGE = arrayOf(
		Manifest.permission.WRITE_EXTERNAL_STORAGE,
		Manifest.permission.READ_EXTERNAL_STORAGE,
	)
	
}