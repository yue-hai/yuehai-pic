package com.yuehai.pic.bean.global

import android.Manifest

object Permissions {
	
	// 存储卡读写权限
	val permissions_storage = arrayOf(
		Manifest.permission.WRITE_EXTERNAL_STORAGE,
		Manifest.permission.READ_EXTERNAL_STORAGE,
	)
	
}