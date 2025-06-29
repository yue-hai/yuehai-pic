package com.yuehai.pic.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yuehai.pic.R
import com.yuehai.pic.bean.global.Config.VIEW_MODE
import com.yuehai.pic.bean.global.Permissions.PERMISSIONS_STORAGE
import com.yuehai.pic.ui.activity.adapter.HomeContentViewPager2Adapter
import com.yuehai.pic.utils.AppInitializer
import com.yuehai.pic.utils.CreateAlertDialogUtil
import com.yuehai.pic.utils.PermissionUtil
import com.yuehai.pic.utils.ImageUtil


/**
 * @author 月海
 * @create 2023/4/26 19:59
 * 主页面，全部图片
 */
class HomeActivity: AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置内容视图；当前的组件显示哪个视图（窗口）；R 就是 res 包
        setContentView(R.layout.activity_home)
        
        // 调用封装的方法，检查并获取权限
        if (PermissionUtil().checkPermission(this, PERMISSIONS_STORAGE, 0)){
            // 调用方法，初始化应用数据
            AppInitializer().initializeApplicationData(this)
            // 调用方法，创建视图对象
            createView()
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
            // 调用方法，创建视图对象
            createView()
        }else{
            // 调用方法，显示提示对话框
            CreateAlertDialogUtil().storagePermissionErrorDialog(this)
        }
    }
    
    /**
     * 创建视图对象
     */
    private fun createView(){
    
        // 调用方法，获取全部图片数据
        ImageUtil().getImageAll(contentResolver)
        
        // 判断用户选择的视图模式
        when (VIEW_MODE){
            /**
             * 滑动翻页视图模式
             */
            0 -> {
                // 获取翻页视图对象 ViewPager2
                val viewPager = findViewById<ViewPager2>(R.id.home_ViewPager2_content)
                // 将翻页视图对象设置为显示
                viewPager.visibility = View.VISIBLE
                // 构建适配器，并传入适配器实例
                viewPager.adapter = HomeContentViewPager2Adapter(this)
                // 默认开始选中第几个视图
                viewPager.currentItem = 0
    
                // 获取翻页视图的标签栏对象 TableLayout
                val tableLayout = findViewById<TabLayout>(R.id.home_TableLayout_navigation_bar)
                // 将翻页视图的标签栏对象设置为显示
                tableLayout.visibility = View.VISIBLE
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
            /**
             * 点击切换视图模式
             */
            1 -> {
                // 将全部图片 Fragment 设置为显示
                findViewById<FragmentContainerView>(R.id.home_fragment_content_all).visibility = View.VISIBLE
            }
            
        }
        
        
    }

}
