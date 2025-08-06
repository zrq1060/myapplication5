package com.example.myapplication.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.setExitSharedElementCallback
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.details.DetailActivity
import com.example.myapplication.bean.ItemData
import com.example.myapplication.R
import java.io.Serializable

class ListActivity : AppCompatActivity() {
    private var itemList: MutableList<ItemData> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    var position = 0
    private var isReturning: Boolean = false

    private val exitElementCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(
            names: MutableList<String>,
            sharedElements: MutableMap<String, View>
        ) {
            Log.e("aaaaa", "ListActivity-setExitSharedElementCallback")
            // TODO 没有效果
            if (!isReturning) return
            Log.e("aaaaa", "ListActivity-setExitSharedElementCallback===222")
            val viewHolder =
                recyclerView.findViewHolderForAdapterPosition(position) as ListAdapter.ViewHolder


            names.clear()
            names.add(viewHolder.imageView.transitionName)
            names.add(viewHolder.titleView.transitionName)

            sharedElements.clear()
            sharedElements.put(viewHolder.imageView.transitionName, viewHolder.imageView)
            sharedElements.put(viewHolder.titleView.transitionName, viewHolder.titleView)
            isReturning=false

//            if (reenterState != null) {
//                val startingPosition = reenterState!!.getInt(EXTRA_STARTING_ALBUM_POSITION)
//                val currentPosition = reenterState!!.getInt(EXTRA_CURRENT_ALBUM_POSITION)
//                if (startingPosition != currentPosition) {
//                    // Current element has changed, need to override previous exit transitions
//                    val newTransitionName =
//                        GalleryItem.transitionName(DataSource.ITEMS[currentPosition].id)
//                    val newSharedElement = imagesRv.findViewWithTag<View>(newTransitionName)
//                    if (newSharedElement != null) {
//                        names.clear()
//                        names.add(newTransitionName)
//
//                        sharedElements.clear()
//                        sharedElements.put(newTransitionName, newSharedElement)
//                    }
//                }
//                reenterState = null
//            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
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
        isReturning=true
        position = data.getIntExtra("Position", 0)
        Log.e("aaaaa", "ListActivity-onActivityReente=$position")



        recyclerView.scrollToPosition(position)
        // 暂停
        Log.e("aaaaa", "ListActivity-暂停")
        postponeEnterTransition()

        recyclerView.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                recyclerView.viewTreeObserver.removeOnPreDrawListener(this)
                // 开始
                Log.e("aaaaa", "ListActivity-开始")
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
