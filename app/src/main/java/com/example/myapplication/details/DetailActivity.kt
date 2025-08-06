package com.example.myapplication.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.setEnterSharedElementCallback
import androidx.core.app.SharedElementCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.bean.ItemData

class DetailActivity : AppCompatActivity() {
    private var isActivityFinish: Boolean = false

    private val enterElementCallback: SharedElementCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(
            names: MutableList<String?>,
            sharedElements: MutableMap<String?, View>
        ) {
            Log.e("aaaaa", "DetailActivity-监听进入回调-是否是Activity销毁=$isActivityFinish")

            if (isActivityFinish) {
                Log.e("aaaaa", "DetailActivity-监听进入回调-是Activity销毁")

                val recyclerView = viewPager2.getChildAt(0) as RecyclerView
                val currentPosition = viewPager2.currentItem

                val currentViewHolder =
                    recyclerView.findViewHolderForAdapterPosition(currentPosition) as? DetailAdapter.ViewHolder
                if (currentViewHolder != null) {
                    Log.e("aaaaa", "DetailActivity-监听进入回调-当前ViewHolder不为空")
                    val imageView = currentViewHolder.imageView
                    val titleView = currentViewHolder.titleView
                    // 名字
                    names.clear()
                    names.add(imageView.transitionName)
                    names.add(titleView.transitionName)

                    // 控件
                    sharedElements.clear()
                    sharedElements.put(imageView.transitionName, imageView)
                    sharedElements.put(titleView.transitionName, titleView)

                } else {
                    Log.e("aaaaa", "DetailActivity-监听进入回调-！！！！当前ViewHolder为空")
                }
                // 可以不用还原，因为页面已经销毁。
                isActivityFinish = false
            }
        }
    }
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置页面
        setContentView(R.layout.activity_detail)
        // 设置进入监听
        setEnterSharedElementCallback(this, enterElementCallback)
        // 等待ViewPager2布局完成后再开始过渡动画
        Log.e("aaaaa", "DetailActivity-onCreate-暂停")
        postponeEnterTransition()

        // 获取传递的数据
        val targetPosition = intent.getIntExtra("position", 0)
        val itemList = intent.getSerializableExtra("allData") as List<ItemData>

        // 配置水平RecyclerView
        viewPager2 = findViewById(R.id.detail_viewpager2)
        val adapter = DetailAdapter(this, itemList)
        viewPager2.setAdapter(adapter)


        // 滚动到点击的位置
        viewPager2.setCurrentItem(targetPosition, false)


        viewPager2.viewTreeObserver
            .addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    viewPager2.viewTreeObserver.removeOnPreDrawListener(this)
                    // 开始过渡动画
                    // 可以延迟几秒后再设置，那就在这个页面等待几秒中，然后再执行动画。
                    Log.e("aaaaa", "DetailActivity-onCreate-开始")
                    startPostponedEnterTransition()
                    return true
                }
            })
    }

    override fun finishAfterTransition() {
        isActivityFinish = true
        val data = Intent()
        data.putExtra("Position", viewPager2.currentItem)
//        data.putExtra(EXTRA_CURRENT_ALBUM_POSITION, currentPosition)
        setResult(Activity.RESULT_OK, data)
        Log.e("aaaaa", "DetailActivity-finishAfterTransition")

        super.finishAfterTransition()
    }
}
