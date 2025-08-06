package com.example.myapplication.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.setEnterSharedElementCallback
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.bean.ItemData
import com.example.myapplication.list.ListAdapter

class DetailActivity : AppCompatActivity() {
    private var isReturning: Boolean = false

    private val enterElementCallback: SharedElementCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(
            names: MutableList<String?>,
            sharedElements: MutableMap<String?, View>
        ) {
            Log.e("aaaaa", "DetailActivity-setEnterSharedElementCallback==$isReturning")
            // TODO 没有效果
            if (!isReturning) return
            val recyclerView = viewPager2.getChildAt(0) as? RecyclerView
            val currentPosition = viewPager2.currentItem

            val viewHolder =
                recyclerView?.findViewHolderForAdapterPosition(currentPosition) as DetailAdapter.ViewHolder

            names.clear()
            names.add(viewHolder.imageView.transitionName)
            names.add(viewHolder.titleView.transitionName)

            sharedElements.clear()
            sharedElements.put(viewHolder.imageView.transitionName, viewHolder.imageView)
            sharedElements.put(viewHolder.titleView.transitionName, viewHolder.titleView)

            isReturning=false


//            if (isReturning) {
//                val sharedElement = imagePagerAdapter?.getView(currentPosition)!!
//
//                if (startingPosition != currentPosition) {
//                    names.clear()
//                    names.add(ViewCompat.getTransitionName(sharedElement))
//
//                    sharedElements.clear()
//                    sharedElements.put(ViewCompat.getTransitionName(sharedElement), sharedElement)
//                }
//            }
        }
    }
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启用窗口过渡
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        // 设置页面
        setContentView(R.layout.activity_detail)
        setEnterSharedElementCallback(this, enterElementCallback)
        // 等待ViewPager2布局完成后再开始过渡动画
        Log.e("aaaaa", "DetailActivity-暂停")
        postponeEnterTransition()

        // 获取传递的数据
        val targetPosition = intent.getIntExtra("position", 0)
        val itemList = intent.getSerializableExtra("allData") as List<ItemData>

        // 配置水平RecyclerView
        viewPager2 = findViewById<ViewPager2>(R.id.detail_viewpager2)
        val adapter = DetailAdapter(  this, itemList)
        viewPager2.setAdapter(adapter)


        // 滚动到点击的位置
        viewPager2.setCurrentItem(targetPosition, false)


        viewPager2.viewTreeObserver
            .addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    viewPager2.viewTreeObserver.removeOnPreDrawListener(this)
                    // 开始过渡动画
                    // 可以延迟几秒后再设置，那就在这个页面等待几秒中，然后再执行动画。
//                    Log.e("aaaaa", "DetailActivity-开始")
//                    startPostponedEnterTransition()
                    return true
                }
            })
    }

    override fun finishAfterTransition() {
        isReturning = true
        val data = Intent()
        data.putExtra("Position", viewPager2.currentItem)
//        data.putExtra(EXTRA_CURRENT_ALBUM_POSITION, currentPosition)
        setResult(Activity.RESULT_OK, data)
        Log.e("aaaaa", "DetailActivity-finishAfterTransition")

        super.finishAfterTransition()
    }
}
