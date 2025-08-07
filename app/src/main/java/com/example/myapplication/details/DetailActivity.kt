package com.example.myapplication.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.setEnterSharedElementCallback
import androidx.core.app.SharedElementCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.bean.ItemData

class DetailActivity : AppCompatActivity() {
    private lateinit var viewPager2: ViewPager2

    private val enterElementCallback: SharedElementCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(
            names: MutableList<String?>,
            sharedElements: MutableMap<String?, View>
        ) {
            // 说明：
            // 进入到此页面，会调用此会回调。
            // 从此页面返回到上个页面，会先调用finishAfterTransition，然后再调用此。所以可以在此控制sharedElements。
            // 总结：进入到此页面、返回到上个页面，都会调用此方法。
            Log.e("aaaaa", "DetailActivity-监听进入回调")

            // 获取到当前的位置，此处用的是ViewPager的位置，也可以通过记录位置和监听位置改变，来维护位置。
            val recyclerView = viewPager2.getChildAt(0) as RecyclerView
            val currentPosition = viewPager2.currentItem

            val currentPositionViewHolder =
                recyclerView.findViewHolderForAdapterPosition(currentPosition) as? DetailAdapter.ViewHolder
            if (currentPositionViewHolder != null) {
                Log.e("aaaaa", "DetailActivity-监听进入回调-当前ViewHolder不为空")
                val imageView = currentPositionViewHolder.imageView
                val titleView = currentPositionViewHolder.titleView

                // 名字
                names.clear()
                names.add("image_$currentPosition")
                names.add("title_$currentPosition")

                // 控件
                sharedElements.clear()
                sharedElements.put("image_$currentPosition", imageView)
                sharedElements.put("title_$currentPosition", titleView)

            } else {
                Log.e("aaaaa", "DetailActivity-监听进入回调-！！！！当前ViewHolder为空")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置页面
        setContentView(R.layout.activity_detail)
        // 设置进入监听
        setEnterSharedElementCallback(this, enterElementCallback)
        // 等待ViewPager2布局完成后再开始过渡动画
        Log.e("aaaaa", "DetailActivity-onCreate-暂停共享动画")
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
                    // 移除
                    viewPager2.viewTreeObserver.removeOnPreDrawListener(this)
                    // 开始过渡动画
                    // 可以延迟几秒后再设置，那就在这个页面等待几秒中，然后再执行动画。
                    Log.e("aaaaa", "DetailActivity-onCreate-开始共享动画")
                    startPostponedEnterTransition()
                    return true
                }
            })
    }

    override fun finishAfterTransition() {
        // 给上个页面传递要还原的位置
        val data = Intent()
        data.putExtra("Position", viewPager2.currentItem)
        setResult(RESULT_OK, data)
        Log.e("aaaaa", "DetailActivity-finishAfterTransition")

        super.finishAfterTransition()
    }
}
