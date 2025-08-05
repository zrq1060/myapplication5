package com.example.myapplication.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.bean.ItemData
import com.example.myapplication.R

class ListAdapter(
    private val itemList: MutableList<ItemData>,
    private val listener: OnItemClickListener?
) : RecyclerView.Adapter<ListAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 设置值
        val data = itemList[position]
        holder.imageView.setImageResource(data.imageRes)
        holder.titleView.text = data.title
        holder.descriptionView.text = data.description

        // 设置共享元素的transitionName
        ViewCompat.setTransitionName(holder.imageView, "image_$position")
        ViewCompat.setTransitionName(holder.titleView, "title_$position")

        // 设置点击事件
        holder.itemView.setOnClickListener(View.OnClickListener { v: View? ->
            listener?.onItemClick(position)
        })
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById<ImageView>(R.id.iv_image)
        var titleView: TextView = itemView.findViewById<TextView>(R.id.tv_title)
        var descriptionView: TextView = itemView.findViewById<TextView>(R.id.tv_description)
    }
}

interface OnItemClickListener {
    fun onItemClick(position: Int)
}