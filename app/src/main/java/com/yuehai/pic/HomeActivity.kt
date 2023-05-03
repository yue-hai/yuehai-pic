package com.yuehai.pic

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
@author 月海
@create 2023/4/26 19:59
 */
class HomeActivity: AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置内容视图；当前的组件显示哪个视图（窗口）；R 就是 res 包
        setContentView(R.layout.home)

    }

}



















