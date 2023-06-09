package com.yuehai.pic.bean.global

import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * 配置类，应用启动时查询数据库，给属性赋值
 */
object Config {
	
	/**
	 * Glide 配置，设置磁盘缓存策略，默认为不使用磁盘缓存
	 *
	 * DiskCacheStrategy.ALL: 默认的缓存策略。它会将原始图像和转换后的图像都缓存在磁盘上。这是最常见的策略，因为它允许在需要时快速加载图像，并在离线时仍然可用。
	 * DiskCacheStrategy.NONE: 不进行磁盘缓存。每次加载图像时都会从原始数据源（如网络）获取最新的图像，并且不会将图像缓存在本地磁盘上。这种策略适用于那些不希望在本地进行缓存的情况。
	 * DiskCacheStrategy.DATA: 只缓存原始图像数据。转换后的图像不会被缓存。这种策略适用于只需要加载原始数据的情况，例如加载视频帧等。
	 * DiskCacheStrategy.RESOURCE: 只缓存转换后的图像。原始图像数据不会被缓存。这种策略适用于不需要再次解码原始数据的情况，例如加载处理过的图像。
	 * DiskCacheStrategy.AUTOMATIC: 自动根据原始数据源（如网络地址）和资源类型（如动画或静态图像）来选择合适
	 */
	var GLIDE_DISK_CACHE_STRATEGY: DiskCacheStrategy = DiskCacheStrategy.NONE
	
}