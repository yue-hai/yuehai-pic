package com.yuehai.pic.ui.activity

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yuehai.pic.R
import com.yuehai.pic.utils.CreateAlertDialogUtil


/**
 * 设置页 Activity
 */
class SettingsActivity: AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// 设置内容视图；当前的组件显示哪个视图（窗口）；R 就是 res 包
		setContentView(R.layout.activity_settings)

		// 调用方法，判断是否是深色模式，是的话改变控件背景颜色、字体颜色、状态选择器颜色
		darkModeChangeControl()
		
		// 给返回按钮绑定点击事件
		findViewById<ImageButton>(R.id.settings_exit).setOnClickListener { onClickListenerExit() }
		
		// 给视图模式选项绑定点击事件
		findViewById<LinearLayout>(R.id.settings_view_mode).setOnClickListener { onClickListenerViewMode() }
		// 给缓存加载策略选项绑定点击事件
		findViewById<LinearLayout>(R.id.settings_loading_caching_cache_loading_strategy).setOnClickListener { onClickListenerCacheLoadingStrategy() }
	}
	
	/**
	 * 设置返回，点击退出详情页，关闭该 activity
	 */
	private fun onClickListenerExit(){
		finish()
	}
	
	/**
	 * 给视图模式选项绑定点击事件，点击时弹出弹窗，进行选择
	 */
	private fun onClickListenerViewMode(){
		// 调用方法，显示选择视图模式单选框
		CreateAlertDialogUtil().selectViewModeDialog(this)
	}
	
	/**
	 * 给缓存加载策略选项绑定点击事件，点击时弹出弹窗，进行选择
	 */
	private fun onClickListenerCacheLoadingStrategy(){
		// 调用方法，显示选择缓存加载策略单选框
		CreateAlertDialogUtil().selectCacheLoadingStrategyDialog(this)
	}

	/**
	 * 判断是否是深色模式，是的话改变控件背景颜色、字体颜色、状态选择器颜色
	 */
	private fun darkModeChangeControl(){
		// 判断是否是深色模式
		if ((resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_YES) ) !=0){
			// 背景颜色
			findViewById<LinearLayout>(R.id.settings).setBackgroundResource(R.color.yuehai_dark_background)
			
			// 视图模式状态选择器、字体颜色
			findViewById<LinearLayout>(R.id.settings_view_mode).setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			findViewById<TextView>(R.id.settings_view_mode_option).setTextColor(Color.WHITE)
			
			// 缓存加载策略状态选择器、字体颜色
			findViewById<LinearLayout>(R.id.settings_loading_caching_cache_loading_strategy).setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			findViewById<TextView>(R.id.settings_loading_caching_cache_loading_strategy_option).setTextColor(Color.WHITE)
		}
	}

}