package pl.kitek.gallery.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import pl.kitek.gallery.data.GalleryItem
import pl.kitek.gallery.ui.adapter.ImageGridAdapter.ViewHolder
import pl.kitek.gallery.ui.view.AspectRatioImageView
import java.util.Random

class ImageGridAdapter(val items: List<GalleryItem>,
                       val onItemClickListener: OnItemClickListener? = null) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position], onItemClickListener)
    }

    override fun getItemCount() = items.size

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            AspectRatioImageView(parent.context)
                    .apply { scaleType = ImageView.ScaleType.CENTER_CROP }) {

        fun bind(item: GalleryItem, onItemClickListener: OnItemClickListener?) {
            itemView.setOnClickListener({ onItemClickListener?.onClick(item, it) })
            itemView.tag = GalleryItem.transitionName(item.id)
            ViewCompat.setTransitionName(itemView, GalleryItem.transitionName(item.id))
//            Picasso.with(itemView.context).load(item.thumbnailURL).into(itemView as ImageView)

            itemView.setBackgroundColor(Random().nextInt())
        }
    }

    interface OnItemClickListener {
        fun onClick(item: GalleryItem, view: View)
    }
}
