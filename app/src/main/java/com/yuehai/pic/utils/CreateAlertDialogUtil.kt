package com.yuehai.pic.utils

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.yuehai.pic.R
import com.yuehai.pic.ui.activity.HomeActivity
import kotlin.system.exitProcess

/**
 * 创建提醒对话框工具类
 */
class CreateAlertDialogUtil {
	
	/**
	 * 创建提醒对话框
	 * @param context 上下文环境
	 * @param title 对话框的标题文本
	 * @param message 对话框的内容文本
	 * @param buttonListener 按钮的实现逻辑
	 */
	private fun createAlertDialog(
		context: Context,
		title: String,
		message: String,
		buttonListener: DialogInterface.OnClickListener,
	){
		// 获取权限失败，创建一个获取权限失败提示一下；创建提醒对话框的建造器
		val builder = AlertDialog.Builder(context)
		
		// 设置对话框的标题文本
		builder.setTitle(title)
		// 设置对话框的内容文本
		.setMessage(message)
		/**
		 * 设置对话框的确认按钮文本及其点击监听器
		 *
		 * dialog 是一个 AlertDialog.Builder 对象，它用于创建一个提醒对话框。
		 * which 是一个整数值，表示用户点击的按钮的索引。
		 *
		 * 在这里，我们设置了两个按钮：确定和取消。
		 * 当用户点击确定按钮时，which 的值为 DialogInterface.BUTTON_POSITIVE（-1），
		 * 当用户点击取消按钮时，which 的值为 DialogInterface.BUTTON_NEGATIVE（-2）
		 */
		.setPositiveButton(context.getString(R.string.alert_dialog_positive), buttonListener)
		// 设置对话框的否定按钮文本及其点击监听器
		.setNegativeButton(context.getString(R.string.alert_dialog_negative), buttonListener)
		
		// 根据建造器构建提醒对话框对象
		val alertDialog = builder.create()
		
		// 显示提醒对话框
		alertDialog.show()
	}
	
	/**
	 * 获取存储卡读写权限失败提示框
	 */
	fun storagePermissionErrorDialog(context: Context){
		// 标题
		val title: String = context.getString(R.string.alert_dialog_title_storage_permission_error)
		// 内容
		val message: String = context.getString(R.string.alert_dialog_message_storage_permission_error)
		// 确定与取消按钮
		val buttonListener = DialogInterface.OnClickListener { dialog, which ->
			when (which) {
				// 处理确定按钮点击事件的逻辑
				DialogInterface.BUTTON_POSITIVE -> {
					context.startActivity(Intent(context, HomeActivity::class.java))
					exitProcess(0)
				}
				// 处理取消按钮点击事件的逻辑
				DialogInterface.BUTTON_NEGATIVE -> {
					Log.i("月海", "点击了取消")
				}
			}
			// 关闭对话框
			dialog.dismiss()
		}
		
		// 调用方法，传递参数，创建提示框
		createAlertDialog(context, title, message, buttonListener)
	}
	
}