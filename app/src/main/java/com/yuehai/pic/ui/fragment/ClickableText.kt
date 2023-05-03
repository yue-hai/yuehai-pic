package com.yuehai.pic.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.yuehai.pic.R
import com.yuehai.pic.ui.fragment.base.BaseFragment

/**
@author 月海
@create 2023/4/29 19:30
 */
class ClickableText: BaseFragment() {

    /**
     * onCreatView 是碎片的生命周期中的一种状态，在为碎片创建视图（加载布局）时调用
     *
     * LayoutInflater inflater：作用类似于 findViewById()
     *      findViewById（）用来寻找 xml 布局下的具体的控件（Button、TextView等）
     *      LayoutInflater inflater() 用来找 res/layout/ 下的 xml 布局文件
     * ViewGroup container：表示容器，View 放在里面
     * Bundle savedInstanceState：保存当前的状态，在活动的生命周期中，只要离开了可见阶段，活动很可能就会被进程终止，这种机制能保存当时的状态
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 加载 Fragment 布局
        val view = inflater.inflate(R.layout.fragment_clickable_text, container, false)

        // 给 Fragment 的按钮添加事件
        view.findViewById<Button>(R.id.nav_all).setOnClickListener { onClickListenerNavAll() }
        view.findViewById<Button>(R.id.nav_directory).setOnClickListener { onClickListenerNavDirectory() }
        view.findViewById<Button>(R.id.nav_tree).setOnClickListener { onClickListenerNavTree() }
        view.findViewById<Button>(R.id.nav_album).setOnClickListener { onClickListenerNavAlbum() }

        return view
    }

    // 全部
    private fun onClickListenerNavAll(){
        Log.i("导航按钮", "全部")
    }

    // 目录
    private fun onClickListenerNavDirectory(){
        Log.i("导航按钮", "目录")
    }

    // 树
    private fun onClickListenerNavTree(){
        Log.i("导航按钮", "树")
    }

    // 相册
    private fun onClickListenerNavAlbum(){
        Log.i("导航按钮", "相册")
    }



}