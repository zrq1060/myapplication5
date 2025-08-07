package com.example.myapplication.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.setExitSharedElementCallback
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.bean.ItemData
import com.example.myapplication.details.DetailActivity
import java.io.Serializable

class ListActivity : AppCompatActivity() {
    private var itemList: MutableList<ItemData> = mutableListOf()
    private lateinit var recyclerView: RecyclerView

    // 选中的位置
    private var selectedPosition = 0

    //
    private val exitElementCallback = object : SharedElementCallback() {

        override fun onMapSharedElements(
            names: MutableList<String>,
            sharedElements: MutableMap<String, View>
        ) {
            // 说明：
            // 从此页面跳到下个页面，会调用此会回调。
            // 从下个页面返回到此页面，会先调用onActivityReenter，然后再调用此。所以可以在此控制sharedElements。
            // 总结：进入到其它页面、返回到这个页面，都会调用此方法。
            Log.e("aaaaa", "ListActivity-监听退出回调-选中位置：$selectedPosition")


            val selectedPositionViewHolder =
                recyclerView.findViewHolderForAdapterPosition(selectedPosition) as? ListAdapter.ViewHolder
            if (selectedPositionViewHolder != null) {
                Log.e("aaaaa", "ListActivity-监听退出回调-找到${selectedPosition}位置的ViewHolder")

                val imageView = selectedPositionViewHolder.imageView
                val titleView = selectedPositionViewHolder.titleView
                // 名字
                names.clear()
                names.add("image_$selectedPosition")
                names.add("title_$selectedPosition")

                // 控件
                sharedElements.clear()
                sharedElements.put("image_$selectedPosition", imageView)
                sharedElements.put("title_$selectedPosition", titleView)
            } else {
                Log.e(
                    "aaaaa",
                    "ListActivity-监听退出回调-！！！没有找到${selectedPosition}位置的ViewHolder"
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        // 设置退出监听
        setExitSharedElementCallback(this, exitElementCallback)

        // 初始化数据
        initData()
        // 配置RecyclerView
        recyclerView = findViewById<RecyclerView>(R.id.list_recycler)
        val adapter = ListAdapter(itemList, object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                click(position, recyclerView)
            }
        })
        recyclerView.setAdapter(adapter)
    }

    // item 点击
    private fun click(position: Int, recyclerView: RecyclerView) {
        selectedPosition = position
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
                Pair.create<View, String>(viewHolder.imageView, "image_$position"),
                Pair.create<View, String>(viewHolder.titleView, "title_$position")
            )
            // 启动详情页
            startActivity(intent, options.toBundle())
        }
    }

    override fun onActivityReenter(resultCode: Int, data: Intent) {
        super.onActivityReenter(resultCode, data)
        // 页面重返，记录页面重返的选中位置。
        selectedPosition = data.getIntExtra("Position", 0)
        Log.e("aaaaa", "ListActivity-onActivityReenter=重返位置为=$selectedPosition")

        // 暂停
        Log.e("aaaaa", "ListActivity-onActivityReenter=重返位置为=$selectedPosition，暂停共享动画")
        postponeEnterTransition()

        // 滚动
        recyclerView.scrollToPosition(selectedPosition)

        recyclerView.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                // 移除
                recyclerView.viewTreeObserver.removeOnPreDrawListener(this)
                // 开启共享动画
                Log.e(
                    "aaaaa",
                    "ListActivity-onActivityReenter=重返位置为=$selectedPosition，开始共享动画"
                )
                startPostponedEnterTransition()
                return true
            }
        })
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
