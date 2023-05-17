package com.yuehai.pic.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yuehai.pic.ui.fragment.FragmentContentAlbum
import com.yuehai.pic.ui.fragment.FragmentContentAll
import com.yuehai.pic.ui.fragment.FragmentContentDirectory
import com.yuehai.pic.ui.fragment.FragmentContentTree

class HomeContentAdapter(
	/**
	 * FragmentActivity 是一个特殊的 Activity，它提供了一种处理 Fragment 的方法，可以在 API 级别低于 11 的系统版本上使用。
	 * 如果支持的最低系统版本是 API 级别 11 或更高，则可以使用常规 Activity
	 */
	private var fa: FragmentActivity,
	// 所需数据
	private var data: List<String>
): FragmentStateAdapter(fa) {
	
	// 返回要显示的 Fragment 的个数
	override fun getItemCount(): Int {
		return data.size
	}
	
	// 返回要显示的 Fragment 实例
	override fun createFragment(p0: Int): Fragment {
		var fragmentView: Fragment? = null
		
		when(data[p0]){
			"content_all" -> { fragmentView = FragmentContentAll() }
			"content_directory" -> { fragmentView = FragmentContentDirectory() }
			"content_tree" -> { fragmentView = FragmentContentTree() }
			"content_album" -> { fragmentView = FragmentContentAlbum() }
		}
		
		return fragmentView!!
	}
	
	
}