package com.yuehai.pic.utils

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.widget.ViewPager2
import com.yuehai.pic.R
import com.yuehai.pic.bean.global.Config
import com.yuehai.pic.bean.global.Config.SORT_METHOD
import com.yuehai.pic.ui.activity.HomeActivity
import com.yuehai.pic.ui.fragment.FragmentContentAll

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
				// 创建一个新的任务，并清除当前任务栈顶
				val intent = Intent(context.applicationContext, HomeActivity::class.java)
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
				context.startActivity(intent)
				// 关闭当前应用
				(context as Activity).finish()
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
	 * 选择视图模式单选框
	 * @param context 上下文环境
	 */
	fun selectViewModeDialog(context: Context){
		// 选项
		val option = context.resources.getStringArray(R.array.alert_dialog_content_view_mode_option)
		
		// 获取 SharedPreferences 对象
		val sharedPreferences = context.getSharedPreferences(context.resources.getString(R.string.app_name_en), MODE_PRIVATE)
		// 查询 sharedPreferences 中保存的数据，即选中的索引，并赋值，默认选中第一项：滑动翻页视图模式
		var selected: Int = sharedPreferences.getInt("select_view_mode", 0)
		
		// 创建对话框的建造器
		val builder = AlertDialog.Builder(context)
		
		// 设置单选框的标题文本
		with(builder) {
			// 设置单选框的标题文本
			setTitle(context.getString(R.string.alert_dialog_title_select_view_mode))
			// 设置单选框的选项、默认选中的选项、以及点击选项后的事件
			setSingleChoiceItems(option, selected){ _, i ->
				// 记录选择的选项
				selected = i
			}
			// 设置单选框的确定按钮文本及其点击监听器
			setPositiveButton(context.getString(R.string.alert_dialog_positive)){ _, _ ->
				// 保存用户的选择，存储数据需要借助 Editor 类
				val edit = sharedPreferences.edit()
				// 放入数据
				edit.putInt("select_view_mode", selected)
				// 保存数据
				edit.apply()
				
				// 创建一个新的任务，并清除当前任务栈顶
				val intent = Intent(context.applicationContext, HomeActivity::class.java)
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
				context.startActivity(intent)
				// 关闭当前应用
				(context as Activity).finish()
			}
			// 设置单选框的否定按钮文本及其点击监听器
			setNegativeButton(context.getString(R.string.alert_dialog_negative)){ _, _ -> }
		}
		
		// 根据建造器构建单选框对象
		val alertDialog = builder.create()
		
		// 显示单选框
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
		val sharedPreferences = context.getSharedPreferences(context.resources.getString(R.string.app_name_en), MODE_PRIVATE)
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
	
	/**
	 * 选择排序方式单选框
	 * @param context 上下文环境
	 */
	fun selectSortMethodDialog(context: Context){
		// 选项
		val option = context.resources.getStringArray(R.array.alert_dialog_content_select_sort_method_option)
		
		// 获取 SharedPreferences 对象
		val sharedPreferences = context.getSharedPreferences(context.resources.getString(R.string.app_name_en), MODE_PRIVATE)
		// 查询 sharedPreferences 中保存的数据，即选中的索引，并赋值，默认选中第一项：按时间降序
		val selected: Int = sharedPreferences.getInt("select_sort_method", 0)
		
		// 创建对话框的建造器
		val builder = AlertDialog.Builder(context)
		
		// 设置单选框的标题文本
		with(builder) {
			// 设置单选框的标题文本
			setTitle(context.getString(R.string.alert_dialog_title_select_sort_method))
			// 设置单选框的选项、默认选中的选项、以及点击选项后的事件
			setSingleChoiceItems(option, selected){ dialogInterface, i ->
				// 保存用户的选择，存储数据需要借助 Editor 类
				val edit = sharedPreferences.edit()
				// 放入数据
				edit.putInt("select_sort_method", i)
				// 保存数据
				edit.apply()
				
				// 给全局变量赋值
				when(i){
					// 按时间降序
					0 -> { SORT_METHOD = MediaStore.Files.FileColumns.DATE_ADDED + " DESC" }
					// 按时间升序
					1 -> { SORT_METHOD = MediaStore.Files.FileColumns.DATE_ADDED + " ASC" }
					// 按名称降序
					2 -> { SORT_METHOD = MediaStore.Files.FileColumns.TITLE + " DESC" }
					// 按名称升序
					3 -> { SORT_METHOD = MediaStore.Files.FileColumns.TITLE + " ASC" }
				}
				
				// 调用方法，获取全部图片数据
				ImageUtil().getImageAll(context.contentResolver)
				
				// 判断用户选择的视图模式
				when (Config.VIEW_MODE) {
					/**
					 * 滑动翻页视图模式
					 */
					0 -> {
						val homeActivity =  context as HomeActivity
						// 获取 ViewPager2 控件
						val viewPager2 = homeActivity.findViewById<ViewPager2>(R.id.home_ViewPager2_content)
						// 获取 ViewPager2 控件中的第一个（索引 0）视图，即 FragmentContentAll
						val fragmentContentAll = homeActivity.supportFragmentManager.findFragmentByTag(
							"f${viewPager2.adapter?.getItemId(0)}"
						) as FragmentContentAll
						// 调用 FragmentContentAll 中的 refreshDisplayArea 方法刷新视图
						fragmentContentAll.refreshDisplayArea()
					}
					/**
					 * 点击切换视图模式
					 */
					1 -> {
						val fragmentManager = (context as HomeActivity).supportFragmentManager
						val fragmentContentAll = fragmentManager.findFragmentById(R.id.home_fragment_content_all) as FragmentContentAll
						fragmentContentAll.refreshDisplayArea()
					}
				}
				
				// 关闭弹窗
				dialogInterface.cancel()
			}
		}
		
		// 根据建造器构建单选框对象
		val alertDialog = builder.create()
		
		// 显示单选框，设置大小、位置要在 show() 方法之后
		alertDialog.show()
	}
	
}