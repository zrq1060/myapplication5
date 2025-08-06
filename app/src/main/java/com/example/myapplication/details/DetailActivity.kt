package com.example.myapplication.details

import android.os.Bundle
import android.view.ViewTreeObserver
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.bean.ItemData

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启用窗口过渡
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        // 设置页面
        setContentView(R.layout.activity_detail)

        // 获取传递的数据
        val targetPosition = intent.getIntExtra("position", 0)
        val itemList = intent.getSerializableExtra("allData") as List<ItemData>

        // 配置水平RecyclerView
        val viewPager2 = findViewById<ViewPager2>(R.id.detail_viewpager2)
        val adapter = DetailAdapter(itemList)
        viewPager2.setAdapter(adapter)

        // 滚动到点击的位置
        viewPager2.setCurrentItem(targetPosition, false)

        // 等待ViewPager2布局完成后再开始过渡动画
        postponeEnterTransition()
        viewPager2.viewTreeObserver
            .addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    viewPager2.viewTreeObserver.removeOnPreDrawListener(this)
                    // 开始过渡动画
                    // 可以延迟几秒后再设置，那就在这个页面等待几秒中，然后再执行动画。
                    startPostponedEnterTransition()
                    return true
                }
            })
    }
}
