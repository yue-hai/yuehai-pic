package com.yuehai.pic.ui.activity.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yuehai.pic.R
import com.yuehai.pic.bean.global.Global.ANIMATION_DURATION_FOUR_HUNDRED_MS
import com.yuehai.pic.bean.global.Global.DOUBLE_SIZE_IMAGE
import com.yuehai.pic.bean.global.Global.FOURFOLD_SIZE_IMAGE
import com.yuehai.pic.bean.global.Global.ORIGINAL_SIZE_IMAGE
import com.yuehai.pic.bean.global.Global.IMAGE_DATA_LIST
import com.yuehai.pic.bean.global.Global.INITIAL_POSITION
import com.yuehai.pic.ui.activity.adapter.holder.ImageViewerHolder
import com.yuehai.pic.utils.AppInitializer
import com.yuehai.pic.utils.GlideUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs


/**
 * 图片详情页的横向翻页视图适配器
 */
class ImageViewerAdapter(
	// 上下文环境
	private var context: Context
): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
	
	/**
	 * 是否是纵向拖动；true：是；false：否
	 * 以此来判断用户是在翻页还是在上下拖动
	 * 当一开始判断是在纵向拖动时，就屏蔽翻页事件，让用户可以随意拖动图片而不触发翻页
	 */
	private var verticalDragging: Boolean = false
	/**
	 * 是否进入拖动事件；true：是；false：否
	 * 判断是否正在拖动，是则不触发点击事件
	 */
	private var enterDrag: Boolean = false
	
	/**
	 * 是否在向上滑动；true：是；false：否
	 * 保证图片上滑时的缩放逻辑
	 */
	private var wipeUp: Boolean = false
	/**
	 * 是否在向下滑动；true：是；false：否
	 * 保证图片下滑时的缩放逻辑
	 */
	private var slideDown: Boolean = false

	/**
	 * 手指滚动时上次的 X 坐标
	 */
	private var lastX: Float = INITIAL_POSITION
	/**
	 * 手指滚动时上次的 Y 坐标
	 */
	private var lastY: Float = INITIAL_POSITION
	
	/**
	 * 第一次点击时的时间，进入页面时给他赋个初始值
	 */
	private var firstClick: Long? = System.currentTimeMillis()
	
	/**
	 * 单击事件的协程
	 */
	private var jobClickEvent: Job? = null
	
	/**
	 * onCreateViewHolder() 用于创建新的 ViewHolder 对象，当 RecyclerView 需要新的 ViewHolder 时，会调用此方法。
	 * 该方法会创建并初始化 ViewHolder 及其关联的视图，但不会填充视图的内容，因为此时 ViewHolder 尚未绑定到具体数据。
	 * 该方法的返回值是一个 ViewHolder 对象。在该方法中，需要创建一个新的 View 对象，并将其包装在一个新的 ViewHolder 对象中。
	 */
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return ImageViewerHolder(LayoutInflater.from(context).inflate(R.layout.activity_image_viewer_details_item, parent, false))
	}
	
	/**
	 * onBindViewHolder() 用于将数据与 ViewHolder 对象关联起来。当 RecyclerView 需要新的数据绑定到 ViewHolder 时，会调用此方法。
	 * 该方法会提取适当的数据，并使用该数据填充 ViewHolder 的布局。
	 * 该方法的第一个参数是一个 ViewHolder 对象，第二个参数是该 ViewHolder 在列表中的位置。在该方法中，您需要使用适当的数据填充视图。
	 */
	@SuppressWarnings("ClickableViewAccessibility")
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		val imageView = (holder as ImageViewerHolder).imageView
		
		// 使用 Glide 加载图片
		GlideUtil().imageDetails(context, imageView, IMAGE_DATA_LIST[position].id)
		
		// 给每个视图绑定触摸事件
		imageView.setOnTouchListener { _, event ->
			// 判断触摸事件
			when(event.action){
				/**
				 * 手指按下时的处理逻辑
				 */
				MotionEvent.ACTION_DOWN -> { actionDownEvent(event) }
				/**
				 * 手指移动时的处理逻辑
				 */
				MotionEvent.ACTION_MOVE -> { actionMoveEvent(imageView, event) }
				/**
				 * 手指抬起时的处理逻辑
				 */
				MotionEvent.ACTION_UP -> { actionUpEvent(imageView, event) }
			}
			true
		}
		
	}
	
	/**
	 * getItemCount() 用于获取数据集中的元素数量。当 RecyclerView 需要知道列表中有多少项时，会调用此方法。
	 * 该方法的返回值是一个整数，表示数据集中的元素数量。
	 */
	override fun getItemCount(): Int {
		return IMAGE_DATA_LIST.size
	}
	
	/**
	 * onViewRecycled() 用于回收 ViewHolder 对象。当 RecyclerView 需要回收 ViewHolder 时，会调用此方法。
	 * 该方法会释放 ViewHolder 的资源，以便在下一次使用时重新绑定数据。
	 * 该方法的参数是一个 ViewHolder 对象，表示需要回收的视图。
	 */
	override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
		super.onViewRecycled(holder)
		
		// ViewHolder 对象被回收时也销毁对应的 Glide 对象
		val imageView: ImageView = (holder as ImageViewerHolder).imageView
		Glide.with(context).clear(imageView)
	}
	
	/**
	 * 当用户离开 RecyclerView 中的某个 Item 时，会调用 Item 的 onViewDetachedFromWindow 方法。
	 * 这个方法在 Item 从屏幕上移除并不再可见时被调用。
	 * 将图片缩放比例调整至原始比例，并将视图位置平移到初始位置
	 */
	override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
		super.onViewDetachedFromWindow(holder)
		holder.itemView.animate()
			.scaleX(ORIGINAL_SIZE_IMAGE)
			.scaleY(ORIGINAL_SIZE_IMAGE)
			.translationX(INITIAL_POSITION)
			.translationY(INITIAL_POSITION)
			.setDuration(ANIMATION_DURATION_FOUR_HUNDRED_MS)
			.start()
	}
	
	/**
	 * 手指按下事件
	 */
	private fun actionDownEvent(event: MotionEvent): Boolean{
		lastX = event.rawX
		lastY = event.rawY
		return true
	}
	/**
	 * 手指移动事件
	 */
	private fun actionMoveEvent(imageView: ImageView, event: MotionEvent): Boolean{
		/**
		 * 移动的距离 = 本次的坐标 - 上次的坐标
		 * 表示在水平和垂直方向上的滚动距离，单位为像素
		 * 如果为正值，则表示手指向右向下滚动；如果为负值，则表示手指向左向上滚动
		 */
		val distanceX = event.rawX - lastX
		val distanceY = event.rawY - lastY
		
		// 得到移动的距离后，将本次手指滚动到的位置赋值给 lastX 和 lastY，作为下次使用时的上次的位置使用
		lastX = event.rawX
		lastY = event.rawY
		
		// 判断移动的距离是否为 0，为 0 代表是第一次滑动，还未获取到移动的距离，则本次不执行逻辑，直接返回
		if (distanceX == INITIAL_POSITION || distanceY == INITIAL_POSITION){ return true }
		
		// 将是否进入拖动事件 flag 设置为 true，表示已进入拖动事件
		enterDrag = true
		
		// 判断是横向滑动还是纵向滑动；abs(distanceX) < 1：判断此时为纵向滑动
		if (abs(distanceX) < 2){ verticalDragging = true }
		
		// 判断当前图片是否是纵向拖动，是则拦截触摸事件，阻止 RecyclerView 的滚动行为
		if (verticalDragging){ imageView.parent.requestDisallowInterceptTouchEvent(true) }
		
		/**
		 * 判断图片大小
		 * 当图片缩放大小 <= 1 时，才进行拖动时缩放的处理
		 */
		if (imageView.scaleX <= ORIGINAL_SIZE_IMAGE){
			
			// 当向上滑动和向下滑动的状态都为 false 时，才给这两个变量赋值
			if (!slideDown && !wipeUp){
				// 向下滚动时给 slideDown 赋值为 true；向上滚动时给 wipeUp 赋值为 true；
				if (distanceY >= 0){ slideDown = true }else{ wipeUp = true }
			}
			
			// 向下滚动时 distanceY 为正数，取他的负数
			if (slideDown){
				/**
				 * animate()：通过 animate() 方法获取一个 ViewPropertyAnimator 对象，用于设置属性动画效果
				 * translationXBy()：相对于当前位置，设置 X 轴方向上的平移距离；正值向右移动，负值向左移动
				 * translationYBy()：相对于当前位置，设置 Y 轴方向上的平移距离；正值向下移动，负值向上移动
				 * scaleXBy()：相对于当前缩放比例，在 X 轴方向上进行缩放操作，正值放大，负值缩小
				 * scaleYBy()：相对于当前缩放比例，在 Y 轴方向上进行缩放操作，正值放大，负值缩小
				 * setDuration(0)：设置动画持续时间为 0 毫秒。表示动画立即执行完成，没有过渡效果
				 * start()：启动属性动画，开始播放动画效果。
				 */
				imageView.animate()
					.translationXBy(distanceX)
					.translationYBy(distanceY)
					.scaleXBy(-distanceY * 0.0002f)
					.scaleYBy(-distanceY * 0.0002f)
					.setDuration(0)
					.start()
			}
			
			// 向上滚动时 distanceY 为负数，使用原值
			if (wipeUp){
				imageView.animate()
					.translationXBy(distanceX)
					.translationYBy(distanceY)
					.scaleXBy(distanceY * 0.0002f)
					.scaleYBy(distanceY * 0.0002f)
					.setDuration(0)
					.start()
			}
			
			return true
		}
		
		/**
		 * 判断当图片缩放大小 > 1 时，上下滑动事件是否触发，
		 * 若是正在上下滑动，则将图片缩放至原始大小，并将上下滑动的 flag 都取反
		 */
		if (slideDown || wipeUp){
			imageView.animate()
				.scaleX(ORIGINAL_SIZE_IMAGE)
				.scaleY(ORIGINAL_SIZE_IMAGE)
				.setDuration(0)
				.start()
			
			slideDown = !slideDown
			wipeUp = !wipeUp
			
			return true
		}
		
		/**
		 * 当图片缩放大小 > 1 时，若是不在上下滑动，只进行图片的滚动偏移
		 */
		imageView.animate()
			.translationXBy(distanceX)
			.translationYBy(distanceY)
			.setDuration(0)
			.start()
		
		// 当图片放大时，拦截触摸事件，阻止 RecyclerView 的滚动行为
		imageView.parent.requestDisallowInterceptTouchEvent(true)
		
		// 判断是否移动到了左边缘
		if (imageMoveLeftEdge(imageView)){
			// 当前在左边缘，手指向左滚动，滚动视图；手指向右滚动，翻页
			if (distanceX < 0){
				imageView.animate()
					.translationX(-(imageView.pivotX - imageView.pivotX * imageView.scaleX))
					.setDuration(0)
					.start()
			}else{
				imageView.parent.requestDisallowInterceptTouchEvent(false)
			}
		}else if (imageMoveRightEdge(imageView)){
			// 当前在右边缘，手指向右滚动，滚动视图；手指向左滚动，翻页
			if (distanceX > 0){
				// 手指向右滚动，滚动视图
				imageView.animate()
					.translationX(-(imageView.drawable.bounds.width() * imageView.scaleX + (imageView.pivotX - imageView.pivotX * imageView.scaleX) - imageView.width))
					.setDuration(0)
					.start()
			}else{
				imageView.parent.requestDisallowInterceptTouchEvent(false)
			}
		}
		
		return true
	}
	
	/**
	 * 手指抬起事件
	 */
	private fun actionUpEvent(imageView: ImageView, event: MotionEvent): Boolean {
		
		// 判断当前图片是否被缩小，是则缩放至原始大小，并滚动至默认位置
		if (imageView.scaleX <= ORIGINAL_SIZE_IMAGE) {
			imageView.animate()
				.scaleX(ORIGINAL_SIZE_IMAGE)
				.scaleY(ORIGINAL_SIZE_IMAGE)
				.translationX(INITIAL_POSITION)
				.translationY(INITIAL_POSITION)
				.setDuration(0)
				.start()
		}
		
		// 判断是否进入拖动事件，是则不触发点击事件
		if (!enterDrag){
			/**
			 * 判断当次点击的时间距离上一次是否超过 250ms
			 *      <= 250：判定为双击事件，取消单击事件的协程，调用双击事件的方法，并记录本次点击的时间
			 *      > 250：判定为单击事件，调用方法启动单击事件的协程，并记录本次点击的时间
			 */
			firstClick = if ((System.currentTimeMillis() - firstClick!!) <= 250){
				stopClickEventCoroutine()
				doubleClickEvent(imageView, event)
				System.currentTimeMillis()
			}else{
				startClickEventCoroutine()
				System.currentTimeMillis()
			}
		}
		
		// 手指抬起时将是否是纵向拖动、是否在向上滑动、是否在向下滑动、是否进入拖动事件 flag 重置为 false
		verticalDragging = false
		wipeUp = false
		slideDown = false
		enterDrag = false
		
		return true
	}
	
	/**
	 * 启动单击事件 job 的方法
	 */
	private fun startClickEventCoroutine() {
		jobClickEvent = CoroutineScope(Dispatchers.Main).launch {
			delay(250)
			clickEvent()
		}
	}
	
	/**
	 * 取消单击事件 job 的方法
	 */
	private fun stopClickEventCoroutine() {
		jobClickEvent?.cancel()
		jobClickEvent = null
	}
	
	/**
	 * 单击事件
	 */
	private fun clickEvent(){
		// 调用方法，显示和隐藏操作栏、通知栏、导航栏
		showInformation()
	}
	
	/**
	 * 双击事件
	 */
	private fun doubleClickEvent(imageView: ImageView, event: MotionEvent){
		// 将缩放中心点设置为点击的坐标
		imageView.pivotX = event.rawX
		imageView.pivotY = event.rawY
		
		// 判断缩放倍数
		when(imageView.scaleX){
			/**
			 * 图片为原始尺寸，则缩放至 2 倍
			 */
			ORIGINAL_SIZE_IMAGE -> {
				imageView.animate()
					.scaleX(DOUBLE_SIZE_IMAGE)
					.scaleY(DOUBLE_SIZE_IMAGE)
					.setDuration(ANIMATION_DURATION_FOUR_HUNDRED_MS)
					.start()
			}
			/**
			 * 图片为 2 倍，则缩放至 4 倍
			 */
			DOUBLE_SIZE_IMAGE -> {
				// 缩放至 4 倍
				imageView.animate()
					.scaleX(FOURFOLD_SIZE_IMAGE)
					.scaleY(FOURFOLD_SIZE_IMAGE)
					.setDuration(ANIMATION_DURATION_FOUR_HUNDRED_MS)
					.start()
			}
			/**
			 * 图片缩放倍数为其他，则原至原始大小，滚动至默认位置
			 */
			else -> {
				imageView.animate()
					.scaleX(ORIGINAL_SIZE_IMAGE)
					.scaleY(ORIGINAL_SIZE_IMAGE)
					.translationX(INITIAL_POSITION)
					.translationY(INITIAL_POSITION)
					.setDuration(ANIMATION_DURATION_FOUR_HUNDRED_MS)
					.start()
			}
		}

	}
	
	/**
	 * 每个视图的点击事件，显示和隐藏操作栏、通知栏、导航栏
	 */
	private fun showInformation(){
		// 获取 context 转换为 Activity 的对象
		val activity = context as Activity
		
		// 获取作为通知栏背景的控件对象
		val notificationBar = activity.findViewById<TextView>(R.id.represents_the_notification_bar)
		// 获取作为导航栏背景的控件对象
		val navigationBar = activity.findViewById<TextView>(R.id.represents_the_navigation_bar)
		// 获取顶部返回按钮和图片信息等的控件对象
		val topInformation = activity.findViewById<FragmentContainerView>(R.id.image_viewer_fragment_top_information)
		// 获取底部图片操作按钮的控件对象
		val bottomOperation = activity.findViewById<FragmentContainerView>(R.id.image_viewer_fragment_bottom_operation)
		
		// 判断当前通知栏和导航栏是否显示，true：正在显示；false：已经隐藏
		if (AppInitializer().isSystemUIVisible(context)){
			// 调用方法，隐藏通知栏和导航栏
			AppInitializer().hideStatusBar(context)
			
			// 隐藏作为通知栏背景的控件对象、作为导航栏背景的控件对象、顶部返回按钮和图片信息、底部图片操作按钮
			notificationBar.visibility = View.GONE
			navigationBar.visibility = View.GONE
			topInformation.visibility = View.GONE
			bottomOperation.visibility = View.GONE
		}else{
			// 调用方法，显示通知栏和导航栏
			AppInitializer().showStatusBar(context)
			
			// 显示作为通知栏背景的控件对象、作为导航栏背景的控件对象、顶部返回按钮和图片信息、底部图片操作按钮
			notificationBar.visibility = View.VISIBLE
			navigationBar.visibility = View.VISIBLE
			topInformation.visibility = View.VISIBLE
			bottomOperation.visibility = View.VISIBLE
		}
	}
	
	/**
	 * 判断当前图片是否移动至左侧边缘
	 * @return true：已经移动至左侧边缘；false：没有移动至左侧边缘
	 */
	private fun imageMoveLeftEdge(imageView: ImageView): Boolean {
		// 判断是否放大，未放大直接返回 true
		if (imageView.scaleX <= ORIGINAL_SIZE_IMAGE){ return true }
		
		/**
		 * 滑动后的左上角的 X 坐标 = 刚放大没有移动时的左上角的 X 坐标 + 视图在 X 轴上的平移距离
		 * 刚放大没有移动时的左上角的 X 坐标：缩放中心点的 X 坐标（原始图片从中心点到左侧的距离） - 缩放中心点的 X 坐标 * 缩放倍数（图片放大后从中心点到左侧的距离）
		 * translationX：视图在 X 轴上的平移距离，默认是 0,0；右移增加，左移减少；scrollY，下移增加，上移减少
		 */
		val topLeftX = (imageView.pivotX - imageView.pivotX * imageView.scaleX) + imageView.translationX
		
		// 如果滑动后的左上角的 X 坐标 >= 0，则说明当前图片移动至左侧边缘
		if (topLeftX >= 0){ return true }
		
		return false
	}
	
	/**
	 * 判断当前图片是否移动至右侧边缘
	 * @return true：已经移动至右侧边缘；false：没有移动至右侧边缘
	 */
	private fun imageMoveRightEdge(imageView: ImageView): Boolean {
		// 判断是否放大，未放大直接返回 true
		if (imageView.scaleX <= ORIGINAL_SIZE_IMAGE){ return true }
		
		/**
		 * 滑动后的左上角的 X 坐标 = 刚放大没有移动时的左上角的 X 坐标 + 视图在 X 轴上的平移距离
		 * 刚放大没有移动时的左上角的 X 坐标：缩放中心点的 X 坐标（原始图片从中心点到左侧的距离） - 缩放中心点的 X 坐标 * 缩放倍数（图片放大后从中心点到左侧的距离）
		 * translationX：视图在 X 轴上的平移距离，默认是 0,0；右移增加，左移减少；scrollY，下移增加，上移减少
		 */
		val topLeftX = (imageView.pivotX - imageView.pivotX * imageView.scaleX) + imageView.translationX
		
		// 滑动后的右下角的 X 坐标 = 滑动后的左上角的 X 坐标 + 放大后的图片的宽度
		val bottomRightX = topLeftX + imageView.drawable.bounds.width() * imageView.scaleX
		
		// 如果滑动后的右下角的 X 坐标 <= 屏幕的宽度，则说明当前图片移动至右侧边缘
		if (bottomRightX <= imageView.width){ return true }
		
		return false
	}
	
	/**
	 * 判断当前图片是否移动至上边缘
	 * @return true：已经移动至上边缘；false：没有移动至上边缘
	 */
	private fun imageMoveTopEdge(imageView: ImageView): Boolean {
		// 判断是否放大，未放大直接返回 true
		if (imageView.scaleX <= ORIGINAL_SIZE_IMAGE){ return true }
		
		// 原始图片左上角的 Y 坐标 = 屏幕高度/2 - 图片原始高度/2
		val originalImageTopLeftY = imageView.height / 2 - imageView.drawable.bounds.height() / 2
		
		/**
		 * 从中心点到上边缘的距离 = 中心点的 Y 坐标 - 原始图片左上角的 Y 坐标
		 * 放大后从中心点到上边缘的距离 = 从中心点到上边缘的距离 * 缩放倍数
		 * 放大后，没有移动时的左上角的 Y 坐标 = 中心点 Y 坐标 - 放大后从中心点到上边缘的距离
		 */
		val enlargedTopLeftY = imageView.pivotY - (imageView.pivotY - originalImageTopLeftY) * imageView.scaleY

		// 向下滑动的距离
		val distanceSlideY = imageView.scrollY * imageView.scaleY
		
		// 滑动后的左上角的 Y 坐标
		val topLeftY = enlargedTopLeftY - distanceSlideY
		
		// 如果滑动后的左上角的 Y 坐标 >= 0，则说明当前图片移动至上边缘
		if (topLeftY >= 0){ return true }
		
		return false
	}
	
	/**
	 * 判断当前图片是否移动至下边缘
	 * @return true：已经移动至下边缘；false：没有移动至下边缘
	 */
	private fun imageMoveBottomEdge(imageView: ImageView): Boolean {
		// 判断是否放大，未放大直接返回 true
		if (imageView.scaleX <= ORIGINAL_SIZE_IMAGE){ return true }
		
		// 原始图片左上角的 Y 坐标 = 屏幕高度/2 - 图片原始高度/2
		val originalImageTopLeftY = imageView.height / 2 - imageView.drawable.bounds.height() / 2
		
		/**
		 * 从中心点到上边缘的距离 = 中心点的 Y 坐标 - 原始图片左上角的 Y 坐标
		 * 放大后从中心点到上边缘的距离 = 从中心点到上边缘的距离 * 缩放倍数
		 * 放大后，没有移动时的左上角的 Y 坐标 = 中心点 Y 坐标 - 放大后从中心点到上边缘的距离
		 */
		val enlargedTopLeftY = imageView.pivotY - (imageView.pivotY - originalImageTopLeftY) * imageView.scaleY
		
		// 向下滑动的距离
		val distanceSlideY = imageView.scrollY * imageView.scaleY
		
		// 滑动后的右下角的 Y 坐标 = 滑动后的左上角的 Y 坐标 + 放大后的图片的高度
		val bottomRightY = (enlargedTopLeftY - distanceSlideY) + imageView.drawable.bounds.height() * imageView.scaleY
		
		// 如果滑动后的右下角的 Y 坐标 <= 屏幕的高度，则说明当前图片移动至下边缘
		if (bottomRightY <= imageView.height){ return true }
		
		return false
	}
	
	
}