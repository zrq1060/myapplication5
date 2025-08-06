package pl.kitek.gallery.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.ViewPager
import pl.kitek.gallery.R
import pl.kitek.gallery.data.DataSource
import pl.kitek.gallery.ui.MainActivity.Companion.EXTRA_CURRENT_ALBUM_POSITION
import pl.kitek.gallery.ui.MainActivity.Companion.EXTRA_STARTING_ALBUM_POSITION
import pl.kitek.gallery.ui.adapter.ImagePagerAdapter

class ImageActivity : AppCompatActivity() {

    private var isReturning: Boolean = false
    private var startingPosition: Int = 0
    private var currentPosition: Int = 0
    private var imagePagerAdapter: ImagePagerAdapter? = null

    private val enterElementCallback: SharedElementCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String?>, sharedElements: MutableMap<String?, View>) {
            Log.e("aaaaaa","ImageActivity-进入监听")

            if (isReturning) {
                Log.e("aaaaaa","ImageActivity-进入监听-来自返回")
                val sharedElement = imagePagerAdapter?.getView(currentPosition)?:return

                if (startingPosition != currentPosition) {
                    Log.e("aaaaaa","ImageActivity-进入监听-来自返回-位置不相同")
                    names.clear()
                    names.add(ViewCompat.getTransitionName(sharedElement))

                    sharedElements.clear()
                    sharedElements.put(ViewCompat.getTransitionName(sharedElement), sharedElement)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        Log.e("aaaaaa","ImageActivity-暂停")
        ActivityCompat.postponeEnterTransition(this)
        ActivityCompat.setEnterSharedElementCallback(this, enterElementCallback)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)

//        setupToolBar()

        val index = DataSource.ITEMS.indexOfFirst { it.id == intent.getIntExtra(ITEM_ID, 0) }
        startingPosition = if (index > 0) index else 0
        currentPosition = savedInstanceState?.getInt(SAVED_CURRENT_PAGE_POSITION) ?: startingPosition

        imagePagerAdapter = ImagePagerAdapter(this, DataSource.ITEMS, currentPosition)
        viewPager.adapter = imagePagerAdapter
        viewPager.currentItem = currentPosition
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                currentPosition = position
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putInt(SAVED_CURRENT_PAGE_POSITION, currentPosition)
    }

    override fun finishAfterTransition() {
        Log.e("aaaaaa","ImageActivity-finishAfterTransition")
        isReturning = true
        val data = Intent()
        data.putExtra(EXTRA_STARTING_ALBUM_POSITION, startingPosition)
        data.putExtra(EXTRA_CURRENT_ALBUM_POSITION, currentPosition)
        setResult(Activity.RESULT_OK, data)
        super.finishAfterTransition()
    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        item?.let {
//            when (it.itemId) {
//                android.R.id.home -> {
//                    supportFinishAfterTransition()
//                    return true
//                }
//                else -> {
//                }
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

//    private fun setupToolBar() {
//        setSupportActionBar(toolbar)
//        supportActionBar?.apply {
//            title = ""
//            setHomeButtonEnabled(true)
//            setDisplayHomeAsUpEnabled(true)
//            elevation = 0f
//        }
//    }

    companion object {
        const val ITEM_ID = "itemId"

        private const val SAVED_CURRENT_PAGE_POSITION = "current_page_position"
    }
}
