package com.yuehai.pic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.yuehai.pic.ui.adapter.HomeContentAdapter

/**
@author 月海
@create 2023/4/26 19:59
 */
class HomeActivity: AppCompatActivity() {
    
    // 定义 Fragment 需要显示的数据
    private val fragmentData = listOf(
        "content_all",
        "content_directory",
        "content_tree",
        "content_album"
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置内容视图；当前的组件显示哪个视图（窗口）；R 就是 res 包
        setContentView(R.layout.home)
    
        // 获取翻页视图对象
        val viewPager = findViewById<ViewPager2>(R.id.content)
        // 构建适配器
        val homeContentAdapter = HomeContentAdapter(this , fragmentData)
        // 传入适配器实例
        viewPager?.adapter = homeContentAdapter
        // 默认开始选中第几个视图
        viewPager.currentItem = 0

    }

}



















