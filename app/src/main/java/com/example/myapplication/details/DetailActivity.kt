package com.example.myapplication.details

import android.os.Bundle
import android.view.ViewTreeObserver
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.bean.ItemData
import com.example.myapplication.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启用窗口过渡
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        // 设置页面
        setContentView(R.layout.activity_detail)

        // 获取传递的数据
        var targetPosition = intent.getIntExtra("position", 0)
        val itemList = intent.getSerializableExtra("allData") as List<ItemData>

        // 配置水平RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.detail_recycler)
        val adapter = DetailAdapter(itemList)
        recyclerView.setAdapter(adapter)

        // 滚动到点击的位置
        recyclerView.scrollToPosition(targetPosition)

        // 等待RecyclerView布局完成后再开始过渡动画
        postponeEnterTransition()
        recyclerView.getViewTreeObserver()
            .addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    recyclerView.getViewTreeObserver().removeOnPreDrawListener(this)
                    // 开始过渡动画
                    // 可以延迟几秒后再设置，那就在这个页面等待几秒中，然后再执行动画。
                    startPostponedEnterTransition()
                    return true
                }
            })
    }
}
