package com.yuehai.pic.ui.activity

import android.content.ContentUris
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.media.MediaScannerConnection
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.yuehai.pic.R
import com.yuehai.pic.bean.global.Global.IMAGE_DATA_LIST
import com.yuehai.pic.ui.activity.adapter.ImageViewerAdapter
import com.yuehai.pic.utils.AppInitializer
import com.yuehai.pic.utils.ImageUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * 图片详情页 Activity
 */
class ImageViewerActivity: AppCompatActivity() {
	
	/**
	 * 定义当前所在 item 的索引
	 * 初始值为上个 Activity 发送的消息，当翻页时会重新赋值
	 */
	private var position: Int? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// 设置内容视图；当前的组件显示哪个视图（窗口）；R 就是 res 包
		setContentView(R.layout.activity_image_viewer)
		/**
		 * 应用 activity 跳转动画
		 * 参数 1：要进入的的 activity 的进入动画，为 0 则为没有任何动画效果
		 * 参数 2：当前的 activity 的退出动画，为 0 则为没有任何动画效果
		 */
		overridePendingTransition(R.anim.image_details_enter, 0)
		
		// 显示通知栏和导航栏，使得布局延伸到状态栏和导航栏区域，该行代码应在 setContentView() 方法之后调用
		AppInitializer().showStatusBar(this)
		
		// 通过 intent 获取上个 Activity 发送的消息，当前所在 item 的索引
		position = intent.extras!!.getInt("position")
		
		// 获取图片列表控件对象
		val recyclerView = findViewById<RecyclerView>(R.id.image_viewer_RecyclerView)
		/**
		 * 设置线性布局管理器
		 *
		 * 第一个参数 context 表示上下文
		 * 第二个参数 LinearLayoutManager.HORIZONTAL 表示布局方向为水平方向
		 * 第三个参数 false 表示是否反转布局
		 */
		recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
		// 设置适配器；调用方法获取全部图片信息
		recyclerView.adapter = ImageViewerAdapter(this)
		// 关闭更改动画
		(recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
		// 指定 RecyclerView 中显示的第几个子项
		recyclerView.scrollToPosition(position!!)
		// 给 recyclerView 绑定滚动事件监听器
		setupScrollListener(recyclerView)
		
		// 使 RecyclerView 自动贴合页面，即在滚动到边缘时自动停止滚动
		val snapHelper = PagerSnapHelper()
		snapHelper.attachToRecyclerView(recyclerView)
		
		// 调用方法，给图片详细信息弹窗设置内容
		setDetails()
		
		// 获取根视图
		val imageViewer = findViewById<ConstraintLayout>(R.id.activity_image_viewer)
		// 获取重命名弹窗
		val renamePopup = findViewById<LinearLayout>(R.id.image_viewer_rename_popup)
		
		// 为根视图 imageViewer 添加全局布局监听器。
		imageViewer.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
			// 用于存储可见窗口的矩形区域
			private val rect = Rect()
			
			override fun onGlobalLayout() {
				// 获取当前可见窗口的矩形区域
				imageViewer.getWindowVisibleDisplayFrame(rect)
				
				// 获取屏幕高度
				val screenHeight = imageViewer.height
				// 计算软键盘高度
				val keypadHeight = screenHeight - rect.bottom
				
				// 如果软键盘高度超过屏幕高度的 15%
				if (keypadHeight > screenHeight * 0.15) {
					// 改变重命名弹窗的下边距，使其上移
					val layoutParams = renamePopup.layoutParams as ConstraintLayout.LayoutParams
					layoutParams.bottomMargin = keypadHeight
					renamePopup.layoutParams = layoutParams
				} else {
					// 恢复重命名弹窗原始布局位置
					val layoutParams = renamePopup.layoutParams as ConstraintLayout.LayoutParams
					layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.dp_24)
					renamePopup.layoutParams = layoutParams
				}
			}
		})
		
		// 给重命名弹窗的取消按钮绑定点击事件
		findViewById<TextView>(R.id.image_viewer_rename_popup_negative).setOnClickListener{ onClickListenerRenamePopupNegative() }
		// 给重命名弹窗的确定按钮绑定点击事件
		findViewById<TextView>(R.id.image_viewer_rename_popup_positive).setOnClickListener{ onClickListenerRenamePopupPositive() }
	}
	
	/**
	 * 当用户按下 back 键时，默认行为是关闭当前的 Activity，调用此方法
	 */
	override fun finish() {
		super.finish()
		/**
		 * 应用 activity 跳转动画
		 * 参数 1：要进入的的 activity 的进入动画，为 0 则为没有任何动画效果
		 * 参数 2：当前的 activity 的退出动画，为 0 则为没有任何动画效果
		 */
		overridePendingTransition(0, R.anim.image_details_exit)
	}
	
	/**
	 * 给 recyclerView 绑定滚动事件监听器
	 * @param recyclerView 要绑定滚动事件监听器的 recyclerView 控件
	 */
	private fun setupScrollListener(recyclerView: RecyclerView) {
		// 给 recyclerView 绑定滚动事件监听器
		recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			// 滚动状态改变
			override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
				super.onScrollStateChanged(recyclerView, newState)
				
				val layoutManager = recyclerView.layoutManager as LinearLayoutManager
				// 判断当前可见的第一个项目的索引和最后一个项目的索引是否相同，相同则调用方法给图片详细信息弹窗设置内容，传递索引
				if ((layoutManager.findFirstVisibleItemPosition()) == (layoutManager.findLastVisibleItemPosition())){
					// 将当前 item 所在的索引赋值
					position = layoutManager.findFirstVisibleItemPosition()
					// 调用方法，给图片详细信息弹窗设置内容
					setDetails()
				}
			}
			
			// 滚动事件
			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				super.onScrolled(recyclerView, dx, dy)
			}
		})
	}
	
	/**
	 * 给图片时间和详细信息弹窗设置内容
	 */
	private fun setDetails() {
		// 根据传入的索引获取图片信息对象
		val imageData = IMAGE_DATA_LIST[position!!]
		
		// 格式化图片保存日期；imageData.dateAdded * 1000：将秒级的时间戳乘以 1000 转换为毫秒级时间戳
		val dateTime: String = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.getDefault()).format(Date(imageData.dateAdded * 1000))
		
		// 给顶部图片信息（日期、时间）赋值
		val fragmentTopInformation = supportFragmentManager.findFragmentById(R.id.image_viewer_fragment_top_information)
		fragmentTopInformation?.view?.findViewById<TextView>(R.id.top_information_date)?.text = dateTime.split(" ")[0]
		fragmentTopInformation?.view?.findViewById<TextView>(R.id.top_information_time)?.text = dateTime.split(" ")[1]
		
		// 创建一个 BitmapFactory.Options 对象，用于设置图片加载选项
		val options = BitmapFactory.Options()
		// 将 inJustDecodeBounds 设置为 true，这样在加载图片时只获取图片的信息而不加载整张图片
		options.inJustDecodeBounds = true
		// 使用 contentResolver.openInputStream 打开图片的输入流，并设置图片加载选项
		BitmapFactory.decodeStream(contentResolver.openInputStream(
			ContentUris.withAppendedId(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				imageData.id
		)), null, options)
		
		// 获取控件对象，给详细信息弹窗赋值，图片的绝对路径、名称、尺寸、大小、类型、来自哪个包、添加日期
		findViewById<TextView>(R.id.details_title).text = resources.getString(R.string.image_viewer_details_title, imageData.title)
		findViewById<TextView>(R.id.details_data).text = resources.getString(R.string.image_viewer_details_data, imageData.data)
		findViewById<TextView>(R.id.details_aspect).text = resources.getString(R.string.image_viewer_details_aspect, options.outWidth, options.outHeight)
		findViewById<TextView>(R.id.details_size).text = resources.getString(R.string.image_viewer_details_size, imageData.size)
		findViewById<TextView>(R.id.details_type).text = resources.getString(R.string.image_viewer_details_type, imageData.type)
		findViewById<TextView>(R.id.details_bucketDisplayName).text = resources.getString(R.string.image_viewer_details_bucketDisplayName, imageData.bucketDisplayName)
		findViewById<TextView>(R.id.details_dateAdded).text = resources.getString(R.string.image_viewer_details_dateAdded, dateTime)
		
		// 给重命名弹窗文本框赋值
		findViewById<EditText>(R.id.image_viewer_rename_popup_new_name).setText(imageData.title)
	}
	
	/**
	 * 重命名弹窗的取消按钮
	 */
	private fun onClickListenerRenamePopupNegative(){
		// 隐藏重命名弹窗
		findViewById<LinearLayout>(R.id.image_viewer_rename_popup).visibility = View.GONE
		
		// 获取输入法
		val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		// 关闭输入法
		inputMethodManager.hideSoftInputFromWindow(window.peekDecorView().windowToken, 0)
	}
	
	/**
	 * 重命名弹窗的确定按钮
	 */
	private fun onClickListenerRenamePopupPositive(){
		// 获取输入框的内容
		val newNameText = findViewById<EditText>(R.id.image_viewer_rename_popup_new_name).text.toString()
		// 正则表达式匹配 \ / : * < > |，包含则返回 true，不可重命名
		if (Regex("[\\\\/:*<>|]").containsMatchIn(newNameText)){ return }
		
		// 隐藏重命名弹窗
		findViewById<LinearLayout>(R.id.image_viewer_rename_popup).visibility = View.GONE
		// 获取输入法管理器
		val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		// 关闭输入法
		inputMethodManager.hideSoftInputFromWindow(window.peekDecorView().windowToken, 0)

		// 构建原文件的 file 对象
		val oldFile = File(IMAGE_DATA_LIST[position!!].data)
		
		// 新文件的全路径：父路径 + 分隔符 + 输入的文件名 + 后缀名
		val newFilePath = oldFile.parent + File.separator + newNameText + IMAGE_DATA_LIST[position!!].suffixName
		// 构建新文件的 file 对象
		val newFile = File(newFilePath)
		
		// 重命名，成功则执行后续逻辑
		if (oldFile.renameTo(newFile)) {
			// 通过 MediaScannerConnection 扫描新文件，更新媒体库
			MediaScannerConnection.scanFile( applicationContext, arrayOf(newFilePath), null ) { _, _ ->
				// 更新列表中的数据
				val newImageId = ImageUtil().getTheIdAccordingToThePath(contentResolver, newFilePath)
				IMAGE_DATA_LIST[position!!].id = newImageId
				IMAGE_DATA_LIST[position!!].data = newFilePath
				IMAGE_DATA_LIST[position!!].title = newNameText
				IMAGE_DATA_LIST[position!!].fileName = newNameText
				
				// 调用方法，给图片详细信息弹窗设置内容
				setDetails()
			}
		}
	}
	
	
}