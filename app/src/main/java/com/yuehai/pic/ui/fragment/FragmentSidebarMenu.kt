package com.yuehai.pic.ui.fragment

import android.content.ContentUris
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.yuehai.pic.R
import com.yuehai.pic.bean.global.Config.VIEW_MODE
import com.yuehai.pic.bean.global.Global.IMAGE_DATA_LIST
import com.yuehai.pic.ui.activity.HomeActivity
import com.yuehai.pic.ui.activity.SettingsActivity
import com.yuehai.pic.utils.AppInitializer
import kotlin.random.Random

/**
 * 侧边栏
 */
class FragmentSidebarMenu: Fragment() {
	
	/**
	 * 定义按钮对象，等待赋值，方便之后使用
	 */
	var sidebarMenuBarAll: TextView? = null
	var sidebarMenuBarDirectory: TextView? = null
	var sidebarMenuBarTree: TextView? = null
	var sidebarMenuBarAlbum: TextView? = null
	var sidebarMenuBarSettings: TextView? = null
	var sidebarMenuBarExit: TextView? = null
	
	/**
	 * onCreateView 是碎片的生命周期中的一种状态，在为碎片创建视图（加载布局）时调用
	 *
	 * LayoutInflater inflater：作用类似于 findViewById()
	 *      findViewById（）用来寻找 xml 布局下的具体的控件（Button、TextView等）
	 *      LayoutInflater inflater() 用来找 res/layout/ 下的 xml 布局文件
	 * ViewGroup container：表示容器，View 放在里面
	 * Bundle savedInstanceState：保存当前的状态，在活动的生命周期中，只要离开了可见阶段，活动很可能就会被进程终止，这种机制能保存当时的状态
	 */
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		
		// 加载 Fragment 布局
		val view = inflater.inflate(R.layout.sidebar_menu, container, false)

		// 调用方法，判断是否是深色模式，是的话改变控件背景颜色、字体颜色、状态选择器颜色
		darkModeChangeControl(view)
		
		// 获取查询出的图片对象集合中的随机一个元素
		val imageData = IMAGE_DATA_LIST[Random.nextInt(IMAGE_DATA_LIST.size)]
		// 使用图片 id 创建 Uri 对象
		val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageData.id)
		// 使用 Uri 对象创建 Bitmap 对象
		val bitmap = BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri))
		// 给头像控件设置资源，调用方法，并传入一个 Bitmap 对象
		view.findViewById<ImageView>(R.id.sidebar_menu_profile_photo).setImageBitmap(AppInitializer().circularImageView(bitmap))
		
		// 调用方法，给用户名控件文本框设置圆角矩形和背景色
		view.findViewById<TextView>(R.id.sidebar_menu_user_name).background = AppInitializer().roundedRectangleTextView(20f, getColor(requireContext(), R.color.yuehai_light_blue_slightly_dark))
		
		// 给按钮对象赋值
		sidebarMenuBarAll = view.findViewById(R.id.sidebar_menu_bar_all)
		sidebarMenuBarDirectory = view.findViewById(R.id.sidebar_menu_bar_directory)
		sidebarMenuBarTree = view.findViewById(R.id.sidebar_menu_bar_tree)
		sidebarMenuBarAlbum = view.findViewById(R.id.sidebar_menu_bar_album)
		sidebarMenuBarSettings = view.findViewById(R.id.sidebar_menu_settings)
		sidebarMenuBarExit = view.findViewById(R.id.sidebar_menu_exit)
		
		// 默认选中全部图片视图，判断是否是深色模式，是则将全部按钮的背景设置为深灰色、不是则将全部按钮的背景设置为浅灰色
		if (( requireContext().resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_YES) ) !=0){
			sidebarMenuBarAll?.setBackgroundColor(getColor(requireContext(), R.color.yuehai_dark_gray))
		}else{
			sidebarMenuBarAll?.setBackgroundColor(getColor(requireContext(), R.color.yuehai_light_gray))
		}
		
		// 判断当前视图模式，如果是点击切换视图模式，则显示 全部、目录、树、相册 按钮，并给按钮添加点击事件
		if (VIEW_MODE == 1){
			sidebarMenuBarAll?.visibility = View.VISIBLE
			sidebarMenuBarDirectory?.visibility = View.VISIBLE
			sidebarMenuBarTree?.visibility = View.VISIBLE
			sidebarMenuBarAlbum?.visibility = View.VISIBLE
			
			sidebarMenuBarAll?.setOnClickListener { onClickListenerSwitchView(it, "all") }
			sidebarMenuBarDirectory?.setOnClickListener { onClickListenerSwitchView(it, "directory") }
			sidebarMenuBarTree?.setOnClickListener { onClickListenerSwitchView(it, "tree") }
			sidebarMenuBarAlbum?.setOnClickListener { onClickListenerSwitchView(it, "album") }
		}
		
		// 给设置按钮添加点击事件
		sidebarMenuBarSettings?.setOnClickListener { onClickListenerSettings() }
		
		// 给退出按钮添加点击事件
		sidebarMenuBarExit?.setOnClickListener { onClickListenerExit() }
		
		return view
	}
	
	/**
	 * 点击切换视图，点击进入对应的页面，并改变按钮的背景颜色
	 */
	private fun onClickListenerSwitchView(view: View, viewName: String){
		val homeActivity =  context as HomeActivity
		
		// 获取 fragment 视图对象
		val fragmentContentAll = homeActivity.findViewById<FragmentContainerView>(R.id.home_fragment_content_all)
		val fragmentContentDirectory = homeActivity.findViewById<FragmentContainerView>(R.id.home_fragment_content_directory)
		val fragmentContentTree = homeActivity.findViewById<FragmentContainerView>(R.id.home_fragment_content_tree)
		val fragmentContentAlbum = homeActivity.findViewById<FragmentContainerView>(R.id.home_fragment_content_album)
		
		// 将所有视图隐藏
		fragmentContentAll.visibility = View.GONE
		fragmentContentDirectory.visibility = View.GONE
		fragmentContentTree.visibility = View.GONE
		fragmentContentAlbum.visibility = View.GONE
		
		// 判断传递进来的参数，指定显示对应的视图
		when(viewName){
			"all" -> { fragmentContentAll.visibility = View.VISIBLE }
			"directory" -> { fragmentContentDirectory.visibility = View.VISIBLE }
			"tree" -> { fragmentContentTree.visibility = View.VISIBLE }
			"album" -> { fragmentContentAlbum.visibility = View.VISIBLE }
		}
		
		// 判断是否是深色模式
		if (( requireContext().resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_YES) ) !=0){
			// 是深色模式，将全部状态选择器改为暗色模式
			sidebarMenuBarAll?.setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			sidebarMenuBarDirectory?.setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			sidebarMenuBarTree?.setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			sidebarMenuBarAlbum?.setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			sidebarMenuBarSettings?.setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			sidebarMenuBarExit?.setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			// 将点击的按钮的背景变为深灰色
			view.setBackgroundColor(getColor(requireContext(), R.color.yuehai_dark_gray))
		}else{
			// 不是深色模式，将全部状态选择器改为亮色模式
			sidebarMenuBarAll?.setBackgroundResource(R.drawable.selector_sidebar_menu_light_mode)
			sidebarMenuBarDirectory?.setBackgroundResource(R.drawable.selector_sidebar_menu_light_mode)
			sidebarMenuBarTree?.setBackgroundResource(R.drawable.selector_sidebar_menu_light_mode)
			sidebarMenuBarAlbum?.setBackgroundResource(R.drawable.selector_sidebar_menu_light_mode)
			sidebarMenuBarSettings?.setBackgroundResource(R.drawable.selector_sidebar_menu_light_mode)
			sidebarMenuBarExit?.setBackgroundResource(R.drawable.selector_sidebar_menu_light_mode)
			// 将点击的按钮的背景变为浅灰色
			view.setBackgroundColor(getColor(requireContext(), R.color.yuehai_light_gray))
		}
		
		// 关闭侧边栏
		homeActivity.findViewById<DrawerLayout>(R.id.drawer_menu).closeDrawers()
	}
	
	/**
	 * 设置按钮，点击进入设置页面
	 */
	private fun onClickListenerSettings(){
		// 跳转到设置页面
		context?.startActivity(Intent(context, SettingsActivity().javaClass))
	}
	
	/**
	 * 关闭按钮，点击关闭软件
	 */
	private fun onClickListenerExit(){
		activity?.finish()
	}
	
	/**
	 * 判断是否是深色模式，是的话改变控件背景颜色、字体颜色、状态选择器颜色
	 */
	private fun darkModeChangeControl(view: View){
		// 判断是否是深色模式
		if (( requireContext().resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_YES) ) !=0){
			// 修改菜单列表的背景颜色
			view.findViewById<LinearLayout>(R.id.sidebar_menu_control_list).setBackgroundColor(getColor(requireContext(), R.color.yuehai_dark_background))
			
			// 修改全部按钮的字体颜色和状态选择器
			with(view.findViewById<TextView>(R.id.sidebar_menu_bar_all)){
				setTextColor(Color.WHITE)
				setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			}
			// 修改目录按钮的字体颜色和状态选择器
			with(view.findViewById<TextView>(R.id.sidebar_menu_bar_directory)){
				setTextColor(Color.WHITE)
				setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			}
			// 修改树按钮的字体颜色和状态选择器
			with(view.findViewById<TextView>(R.id.sidebar_menu_bar_tree)){
				setTextColor(Color.WHITE)
				setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			}
			// 修改相册按钮的字体颜色和状态选择器
			with(view.findViewById<TextView>(R.id.sidebar_menu_bar_album)){
				setTextColor(Color.WHITE)
				setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			}
			
			// 修改设置按钮的字体颜色和状态选择器
			with(view.findViewById<TextView>(R.id.sidebar_menu_settings)){
				setTextColor(Color.WHITE)
				setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			}
			
			// 修改退出按钮的字体颜色和状态选择器
			with(view.findViewById<TextView>(R.id.sidebar_menu_exit)){
				setTextColor(Color.WHITE)
				setBackgroundResource(R.drawable.selector_sidebar_menu_dark_mode)
			}
		}
	}
	
}