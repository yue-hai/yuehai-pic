package com.yuehai.pic

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yuehai.pic.bean.global.Permissions
import com.yuehai.pic.ui.adapter.HomeContentViewPager2Adapter
import com.yuehai.pic.utils.PermissionUtil
import kotlin.system.exitProcess


/**
@author 月海
@create 2023/4/26 19:59
 */
class HomeActivity: AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置内容视图；当前的组件显示哪个视图（窗口）；R 就是 res 包
        setContentView(R.layout.home)

        // 调用封装的方法，检查并获取权限
        if (PermissionUtil().checkPermission(this, Permissions.permissions_storage, 0)){
            // 调用方法，创建翻页视图对象 ViewPager2
            createViewPager()
        }
    
    }
    
    /**
     * 用户选择权限结果后会调用该回调方法
     *
     * @param requestCode：权限申请识别码
     * @param permissions：权限列表
     * @param grantResults：权限申请结果数组
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    
        // 调用封装的方法，判断权限申请是否成功
        if (PermissionUtil().checkGrant(grantResults)){
            // 调用方法，创建翻页视图对象 ViewPager2
            createViewPager()
        }else{
            // 获取权限失败，创建一个获取权限失败提示一下；创建提醒对话框的建造器
            val builder = AlertDialog.Builder(this)
    
            // 设置对话框的标题文本
            builder.setTitle("获取存储卡读写权限失败")
            // 设置对话框的内容文本
            builder.setMessage("""
                这是一个图片管理器
                没有权限就做不到任何事情
                重启软件可以重新获取权限
                点击确定按钮重新启动软件
                点击取消按钮不会发生任何事
            """.trimIndent())
    
            /**
             * 设置对话框的肯定按钮文本及其点击监听器
             *
             * dialog 是一个 AlertDialog.Builder 对象，它用于创建一个提醒对话框。
             * which 是一个整数值，表示用户点击的按钮的索引。
             *
             * 在这里，我们设置了两个按钮：确定和取消。
             * 当用户点击确定按钮时，which 的值为 DialogInterface.BUTTON_POSITIVE（-1），当
             * 用户点击取消按钮时，which 的值为 DialogInterface.BUTTON_NEGATIVE（-2）
             */
            builder.setPositiveButton("确定"){ _, _ ->
                // 使用 Intent 启动应用的主界面，然后调用 exitProcess(0)来结束当前的进程和活动
                startActivity(Intent(this, HomeActivity::class.java))
                exitProcess(0)
            }
            // 设置对话框的否定按钮文本及其点击监听器
            builder.setNegativeButton("取消"){ _, _ -> Log.d("月海", "点击了取消") }
    
            // 根据建造器构建提醒对话框对象
            val alertDialog = builder.create()
            // 显示提醒对话框
            alertDialog.show()
        }
    }
    
    /**
     * 创建翻页视图对象 ViewPager2
     */
    private fun createViewPager(){
        // 获取翻页视图对象 ViewPager2
        val viewPager = findViewById<ViewPager2>(R.id.home_ViewPager2_content)
        // 构建适配器
        val homeContentViewPager2Adapter = HomeContentViewPager2Adapter(this, contentResolver)
        // 传入适配器实例
        viewPager.adapter = homeContentViewPager2Adapter
        // 默认开始选中第几个视图
        viewPager.currentItem = 0
    
        // 获取翻页视图的标签栏对象 TableLayout
        val tableLayout = findViewById<TabLayout>(R.id.home_TableLayout_navigation_bar)
    
        /**
         * 创建 TabLayoutMediator 对象
         *
         * 参数 1：翻页视图的标签栏对象 TableLayout
         * 参数 2：翻页视图对象 ViewPager2
         * 参数 3：Lambda 表达式，根据索引设置对应的标签的标题
         */
        val tabLayoutMediator = TabLayoutMediator(tableLayout, viewPager) { tab, position ->
            // 根据位置设置标签的标题
            when(position){
                // 全部
                0 -> { tab.text = getString(R.string.navigation_bar_all) }
                // 目录
                1 -> { tab.text = getString(R.string.navigation_bar_directory) }
                // 树
                2 -> { tab.text = getString(R.string.navigation_bar_tree) }
                // 相册
                3 -> { tab.text = getString(R.string.navigation_bar_album) }
            }
        }
        // 将此标签栏依附到翻页视图对象上
        tabLayoutMediator.attach()
    }

}



















