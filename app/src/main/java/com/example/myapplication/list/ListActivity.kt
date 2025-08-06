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

    // 页面重返要还原的位置
    private var activityReenterPosition = 0

    // 是否是页面重返
    private var isActivityReenter: Boolean = false

    private val exitElementCallback = object : SharedElementCallback() {

        override fun onMapSharedElements(
            names: MutableList<String>,
            sharedElements: MutableMap<String, View>
        ) {
            Log.e("aaaaa", "ListActivity-监听退出回调-是否是Activity重返=$isActivityReenter")
            if (isActivityReenter) {
                Log.e("aaaaa", "ListActivity-监听退出回调-是Activity重返")

                val activityReenterPositionViewHolder =
                    recyclerView.findViewHolderForAdapterPosition(activityReenterPosition) as? ListAdapter.ViewHolder
                if (activityReenterPositionViewHolder != null) {
                    Log.e(
                        "aaaaa",
                        "ListActivity-监听退出回调-是Activity重返，找到重返位置的ViewHolder"
                    )

                    val imageView = activityReenterPositionViewHolder.imageView
                    val titleView = activityReenterPositionViewHolder.titleView
                    // 名字
                    names.clear()
                    names.add(imageView.transitionName)
                    names.add(titleView.transitionName)

                    // 控件
                    sharedElements.clear()
                    sharedElements.put(imageView.transitionName, imageView)
                    sharedElements.put(titleView.transitionName, titleView)
                } else {
                    Log.e(
                        "aaaaa",
                        "ListActivity-监听退出回调-是Activity重返，！！！没有找到重返位置的ViewHolder"
                    )
                }

                // 重返回的还原，避免再次进入动画有问题。
                isActivityReenter = false
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

    override fun onActivityReenter(resultCode: Int, data: Intent) {
        super.onActivityReenter(resultCode, data)
        // 标记为页面重返，和记录页面重返位置。
        isActivityReenter = true
        activityReenterPosition = data.getIntExtra("Position", 0)
        Log.e("aaaaa", "ListActivity-onActivityReenter=重返位置为=$activityReenterPosition")

        // 暂停
        Log.e("aaaaa", "ListActivity-onActivityReenter=重返位置为=$activityReenterPosition，暂停")
        postponeEnterTransition()

        recyclerView.scrollToPosition(activityReenterPosition)

        recyclerView.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                recyclerView.viewTreeObserver.removeOnPreDrawListener(this)
                // 开始
                Log.e("aaaaa", "ListActivity-onActivityReenter=重返位置为=$activityReenterPosition，开始")
                startPostponedEnterTransition()
                return true
            }
        })

//        reenterState = Bundle(data.extras)
//        reenterState?.let {
//            val startingPosition = it.getInt(EXTRA_STARTING_ALBUM_POSITION)
//            val currentPosition = it.getInt(EXTRA_CURRENT_ALBUM_POSITION)
//            if (startingPosition != currentPosition) imagesRv.scrollToPosition(currentPosition)
//            ActivityCompat.postponeEnterTransition(this)
//
//            imagesRv.viewTreeObserver.addOnPreDrawListener(object :
//                ViewTreeObserver.OnPreDrawListener {
//                override fun onPreDraw(): Boolean {
//                    imagesRv.viewTreeObserver.removeOnPreDrawListener(this)
//                    ActivityCompat.startPostponedEnterTransition(this@MainActivity)
//                    return true
//                }
//            })
//        }
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
