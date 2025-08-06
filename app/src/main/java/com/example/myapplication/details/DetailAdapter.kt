package com.example.myapplication.details

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.bean.ItemData
import com.example.myapplication.R

class DetailAdapter(val activity: DetailActivity,private val itemList: List<ItemData>) :
    RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemList[position]
        // 设置值
        holder.imageView.setImageResource(data.imageRes)
        holder.titleView.text = data.title
        holder.descriptionView.text = data.description

        // 设置共享元素的transitionName，与列表项保持一致
        ViewCompat.setTransitionName(holder.imageView, "image_$position")
        ViewCompat.setTransitionName(holder.titleView, "title_$position")

        holder.imageView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                holder.imageView.viewTreeObserver.removeOnPreDrawListener(this)
                Log.e("aaaaaa","DetailActivity-开始")
                ActivityCompat.startPostponedEnterTransition(activity)
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById<ImageView>(R.id.iv_detail_image)
        var titleView: TextView = itemView.findViewById<TextView>(R.id.tv_detail_title)
        var descriptionView: TextView = itemView.findViewById<TextView>(R.id.tv_detail_description)
    }
}
