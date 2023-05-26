package com.yuehai.pic.ui.adapter

import android.content.ContentResolver
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yuehai.pic.ui.fragment.FragmentContentAlbum
import com.yuehai.pic.ui.fragment.FragmentContentAll
import com.yuehai.pic.ui.fragment.FragmentContentDirectory
import com.yuehai.pic.ui.fragment.FragmentContentTree
import com.yuehai.pic.utils.PictureUtil

class HomeContentViewPager2Adapter(
	/**
	 * fa 是 FragmentStateAdapter 的一个构造函数参数，它是一个 FragmentActivity 类型的对象。可以进行的操作有：
	 *  调用 fa 的方法，比如 startActivity，finish，getSupportFragmentManager 等。
	 *  访问 fa 的属性，比如 lifecycle，supportActionBar，intent 等。
	 *  注册或取消注册 fa 的生命周期观察者，比如 fa.lifecycle.addObserver 或 fa.lifecycle.removeObserver。
	 *  在 fa 中创建或销毁 Fragment，比如 fa.supportFragmentManager.beginTransaction().add 或 fa.supportFragmentManager.beginTransaction().remove。
	 */
	fa: FragmentActivity,
	private val contentResolver: ContentResolver
): FragmentStateAdapter(fa) {
	
	// 返回要显示的 Fragment 的个数
	override fun getItemCount(): Int {
		return 4
	}
	
	// 返回要显示的 Fragment 实例
	override fun createFragment(p0: Int): Fragment {
		// 定义 Fragment 实例，等待赋值
		var fragmentView: Fragment? = null
		
		// 根据索引判断创建哪个 Fragment 实例，并赋值
		when(p0){
			// 全部
			0 -> {
				fragmentView = FragmentContentAll()
				PictureUtil().getImageAll(contentResolver)
			}
			// 目录
			1 -> { fragmentView = FragmentContentDirectory() }
			// 树
			2 -> { fragmentView = FragmentContentTree() }
			// 相册
			3 -> { fragmentView = FragmentContentAlbum() }
		}
		
		// 返回 Fragment 实例
		return fragmentView!!
	}
	
	
}