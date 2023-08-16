package com.yuehai.pic.ui.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuehai.pic.R
import com.yuehai.pic.bean.global.Global.IMAGE_DATA_LIST
import com.yuehai.pic.utils.AppInitializer

/**
 * 图片详情页的底部操作栏
 */
class FragmentImageViewerBottomOperation : Fragment() {
	
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
		val view = inflater.inflate(R.layout.fragment_image_viewer_bottom_operation, container, false)
		
		// 获取根布局，设置下边距
		val bottomInformation = view.findViewById<LinearLayout>(R.id.fragment_image_viewer_bottom_operation)
		val linearLayout = bottomInformation.layoutParams as FrameLayout.LayoutParams
		linearLayout.bottomMargin = AppInitializer().getNavigationBarHeight(context as Activity)
		view.layoutParams = linearLayout
		
		// 给移动按钮添加点击事件
		view.findViewById<TextView>(R.id.bottom_information_move).setOnClickListener{ onClickListenerMove() }
		// 给删除按钮添加点击事件
		view.findViewById<TextView>(R.id.bottom_information_delete).setOnClickListener{ onClickListenerDelete() }
		// 给改名按钮添加点击事件
		view.findViewById<TextView>(R.id.bottom_information_rename).setOnClickListener{ onClickListenerRename() }
		// 给编辑按钮添加点击事件
		view.findViewById<TextView>(R.id.bottom_information_edit).setOnClickListener{ onClickListenerEdit() }
		// 给分享按钮添加点击事件
		view.findViewById<TextView>(R.id.bottom_information_sending).setOnClickListener{ onClickListenerSending() }
		
		return view
	}
	
	/**
	 * 获取当前显示的图片的索引
	 */
	private fun indexOfDisplayedImages(): Int{
		val layoutManager = activity?.findViewById<RecyclerView>(R.id.image_viewer_RecyclerView)?.layoutManager as LinearLayoutManager
		// 获取当前可见的第一个项目的索引
		val firstPosition = layoutManager.findFirstVisibleItemPosition()
		// 获取当前可见的最后一个项目的索引
		val lastPosition = layoutManager.findLastVisibleItemPosition()
		
		// 判断第一个和最后一个索引相同，则返回索引，否则返回 -1
		if (firstPosition == lastPosition) return firstPosition
		
		return -1
	}
	
	/**
	 * 移动按钮
	 */
	private fun onClickListenerMove(){
	}
	
	/**
	 * 删除按钮
	 */
	private fun onClickListenerDelete(){
	}
	
	/**
	 * 改名按钮
	 */
	private fun onClickListenerRename(){
		// 获取当前显示的图片的索引
		val indexOfDisplayedImages = indexOfDisplayedImages()
		// 索引为 -1 则停止执行方法
		if (indexOfDisplayedImages == -1) return
		
		// 获取重命名弹窗、文本框控件
		val renamePopup = activity?.findViewById<LinearLayout>(R.id.image_viewer_rename_popup)
		val newName = activity?.findViewById<EditText>(R.id.image_viewer_rename_popup_new_name)

		// 显示重命名弹窗
		renamePopup?.visibility = View.VISIBLE
		
		// 将图片名字赋值给文本框控件
		newName?.setText(IMAGE_DATA_LIST[indexOfDisplayedImages].fileName)
		// 使文本框获取焦点
		newName?.requestFocus()
		
		// 获取输入法
		val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		// 打开输入法，接受软键盘输入的编辑文本或其它视图
		inputMethodManager.showSoftInput(newName, InputMethodManager.SHOW_FORCED)
	}
	
	/**
	 * 编辑按钮
	 */
	private fun onClickListenerEdit(){
	}
	
	/**
	 * 分享按钮
	 */
	private fun onClickListenerSending(){
	}
	
}