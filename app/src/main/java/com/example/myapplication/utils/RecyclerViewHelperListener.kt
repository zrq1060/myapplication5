package com.example.myapplication.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * 描述:
 *
 * @author zhangrq
 * 2017/11/17 9:31
 */
class RecyclerViewHelperListener(private val recyclerView: RecyclerView, private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    private var move: Boolean = false
    private var mIndex: Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        //在这里进行第二次滚动（最后的100米！）
        if (move) {
            move = false
            //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
            val n = mIndex - layoutManager.findFirstVisibleItemPosition()
            if (0 <= n && n < this.recyclerView.childCount) {
                //获取要置顶的项顶部离RecyclerView顶部的距离
                if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
                    // 垂直的
                    val top = this.recyclerView.getChildAt(n).top
                    //最后的移动
                    this.recyclerView.scrollBy(0, top)
                } else {
                    // 水平的
                    val left = this.recyclerView.getChildAt(n).left
                    //最后的移动
                    this.recyclerView.scrollBy(left, 0)
                }
            }
        }
    }

    @JvmOverloads
    fun scrollToPosition(position: Int, isSmooth: Boolean = false) {
        mIndex = position

        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        val firstItem = layoutManager.findFirstVisibleItemPosition()
        val lastItem = layoutManager.findLastVisibleItemPosition()
        //然后区分情况
        if (position < firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            if (isSmooth) recyclerView.smoothScrollToPosition(position) else recyclerView.scrollToPosition(position)
        } else if (position <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
                // 垂直滚动
                val top = recyclerView.getChildAt(position - firstItem).top
                if (isSmooth) recyclerView.smoothScrollBy(0, top) else recyclerView.scrollBy(0, top)
            } else {
                // 水平滚动
                val left = recyclerView.getChildAt(position - firstItem).left
                if (isSmooth) recyclerView.smoothScrollBy(left, 0) else recyclerView.scrollBy(left, 0)
            }
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            if (isSmooth) recyclerView.smoothScrollToPosition(position) else recyclerView.scrollToPosition(position)
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true
        }
    }
}