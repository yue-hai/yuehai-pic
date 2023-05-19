package com.yuehai.pic.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtil {
	
	/**
	 * 检查权限
	 * @param activity 当前 activity
	 * @param permissions 权限数组
	 * @param requestCode 权限申请识别码
	 * @return 返回 true 表示完全启用权限，返回 false 则表示未完全启用所有权限
	 */
	fun checkPermission(activity: Activity, permissions: Array<String>, requestCode: Int): Boolean{
		
		// 获取权限检查值
		var check = PackageManager.PERMISSION_GRANTED
		
		// 循环判断权限是否存在
		for (permission in permissions) {
			// 获取权限检查结果，给权限检查值赋值
			check = ContextCompat.checkSelfPermission(activity, permission)
			// 判断权限是否存在
			if (check != PackageManager.PERMISSION_GRANTED){
				// 数组中有一个权限不存在，就结束循环
				break
			}
		}
		
		/**
		 * 判断权限检查值，若有权限不存在，则请求系统弹窗，好让用户选择是否开启权限
		 *
		 * 为什么不在循环里请求权限的原因是：
		 *      判断权限是一个一个判断的
		 *      请求权限是可以一组一起获取的
		 */
		if (check != PackageManager.PERMISSION_GRANTED){
			// 请求权限
			ActivityCompat.requestPermissions(activity, permissions, requestCode)
			return false
		}
		
		// 所有权限都存在
		return true
	}
	
	/**
	 * 根据权限申请回调函数返回的权限申请结果数组，判断申请是否成功
	 *
	 * @param grantResults 权限申请结果数组
	 * @return 返回 true 表示都已经授权
	 */
	fun checkGrant(grantResults: IntArray): Boolean {
		// 遍历数组
		grantResults.forEach {
			// 判断结果，权限是否申请成功
			if (it != PackageManager.PERMISSION_GRANTED){
				// 权限申请失败
				return false
			}
		}
		
		// 权限申请成功
		return true
	}
	
}