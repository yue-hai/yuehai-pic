package com.yuehai.pic.ui.activity

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.yuehai.pic.R

class SettingsActivity: AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// 设置内容视图；当前的组件显示哪个视图（窗口）；R 就是 res 包
		setContentView(R.layout.activity_settings)
		
		// 给返回按钮绑定点击事件
		findViewById<ImageButton>(R.id.settings_exit).setOnClickListener { onClickListenerExit() }
	}
	
	/**
	 * 设置返回，点击退出详情页，关闭该 activity
	 */
	private fun onClickListenerExit(){
		finish()
	}
}