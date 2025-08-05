package com.example.myapplication.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.details.DetailActivity
import com.example.myapplication.bean.ItemData
import com.example.myapplication.R
import java.io.Serializable

class ListActivity : AppCompatActivity() {
    private var itemList: MutableList<ItemData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        // 初始化数据
        initData()
        // 配置RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.list_recycler)
        val adapter = ListAdapter(itemList, object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                click(position, recyclerView)
            }
        })
        recyclerView.setAdapter(adapter)
    }

    // item 点击
    private fun click(position: Int, recyclerView: RecyclerView) {
        // 处理点击事件，跳转到详情页
        val intent = Intent(this@ListActivity, DetailActivity::class.java)
        intent.putExtra("position", position)
        intent.putExtra("allData", itemList as Serializable)

        // 获取当前点击项的视图
        val viewHolder =
            recyclerView.findViewHolderForAdapterPosition(position) as ListAdapter.ViewHolder?
        if (viewHolder != null) {
            // 创建共享元素过渡
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@ListActivity,
                Pair.create<View?, String?>(viewHolder.imageView as View?, "image_$position"),
                Pair.create<View?, String?>(viewHolder.titleView as View?, "title_$position")
            )

            // 启动详情页
            startActivity(intent, options.toBundle())
        }
    }

    // 初始化列表数据
    private fun initData() {
        itemList.add(ItemData(R.mipmap.ic_launcher, "项目 1", "这是第一个项目的详细描述信息"))
        itemList.add(ItemData(R.mipmap.ic_launcher, "项目 2", "这是第二个项目的详细描述信息"))
        itemList.add(ItemData(R.mipmap.ic_launcher, "项目 3", "这是第三个项目的详细描述信息"))
        itemList.add(ItemData(R.mipmap.ic_launcher, "项目 4", "这是第四个项目的详细描述信息"))
        itemList.add(ItemData(R.mipmap.ic_launcher, "项目 5", "这是第五个项目的详细描述信息"))
    }
}
