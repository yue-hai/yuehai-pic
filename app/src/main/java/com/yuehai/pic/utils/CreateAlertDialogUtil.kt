package com.yuehai.pic.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.yuehai.pic.R
import com.yuehai.pic.ui.activity.HomeActivity
import kotlin.system.exitProcess

/**
 * 创建提醒对话框工具类
 */
class CreateAlertDialogUtil {
	
	// 创建一个空的 Toast 消息对象
	private var makeText: Toast? = null

	/**
	 * 获取存储卡读写权限失败提示框
	 * @param context 上下文环境
	 */
	fun storagePermissionErrorDialog(context: Context){
		// 创建对话框的建造器
		val builder = AlertDialog.Builder(context)
		
		// 设置对话框属性
		with(builder) {
			// 设置对话框的标题文本
			setTitle(context.getString(R.string.alert_dialog_title_storage_permission_error))
			// 设置对话框的内容文本
			setMessage(context.getString(R.string.alert_dialog_content_storage_permission_error))
			// 设置对话框的确认按钮文本及其点击监听器
			setPositiveButton(context.getString(R.string.alert_dialog_positive)){ _, _ ->
				context.startActivity(Intent(context, HomeActivity::class.java))
				exitProcess(0)
			}
			// 设置对话框的否定按钮文本及其点击监听器；还有第三个其他按钮：setNeutralButton
			setNegativeButton(context.getString(R.string.alert_dialog_negative)){ _, _ -> Log.i("月海", "点击了取消") }
		}
		
		// 根据建造器构建提醒对话框对象
		val alertDialog = builder.create()
		
		// 显示提醒对话框
		alertDialog.show()
	}
	
	/**
	 * 选择缓存加载策略单选框
	 * @param context 上下文环境
	 */
	fun selectCacheLoadingStrategyDialog(context: Context){
		// 选项
		val option = context.resources.getStringArray(R.array.alert_dialog_content_select_cache_loading_strategy_option)
		// 选项说明
		val description = context.resources.getStringArray(R.array.alert_dialog_content_select_cache_loading_strategy_description)
		
		// 获取 SharedPreferences 对象
		val sharedPreferences = context.getSharedPreferences("yuehai-pic", MODE_PRIVATE)
		// 查询 sharedPreferences 中保存的数据，即选中的索引，并赋值，默认选中第二项：不进行磁盘缓存
		var selected: Int = sharedPreferences.getInt("select_cache_loading_strategy", 1)
		
		// 创建对话框的建造器
		val builder = AlertDialog.Builder(context)
		
		// 设置单选框的标题文本
		with(builder) {
			// 设置单选框的标题文本
			setTitle(context.getString(R.string.alert_dialog_title_select_cache_loading_strategy))
			// 设置单选框的选项、默认选中的选项、以及点击选项后的事件
			setSingleChoiceItems(option, selected){ _, i ->
				// 记录选择的选项
				selected = i
				
				// 关闭之前的消息提示
				makeText?.cancel()
				// 给 Toast 消息对象实例化赋值
				makeText = Toast.makeText(context, description[i], Toast.LENGTH_LONG)
				// 显示 Toast 消息提示框
				makeText?.show()
			}
			// 设置单选框的确定按钮文本及其点击监听器
			setPositiveButton(context.getString(R.string.alert_dialog_positive)){ _, _ ->
				// 保存用户的选择，存储数据需要借助 Editor 类
				val edit = sharedPreferences.edit()
				// 放入数据
				edit.putInt("select_cache_loading_strategy", selected)
				// 保存数据
				edit.apply()
				
				// 关闭消息提示
				makeText?.cancel()
			}
			// 设置单选框的否定按钮文本及其点击监听器
			setNegativeButton(context.getString(R.string.alert_dialog_negative)){ _, _ ->
				// 关闭消息提示
				makeText?.cancel()
			}
		}
		
		// 根据建造器构建单选框对象
		val alertDialog = builder.create()
		
		// 显示单选框
		alertDialog.show()
	}
	
}