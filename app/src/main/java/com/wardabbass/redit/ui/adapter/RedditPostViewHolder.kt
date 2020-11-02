package com.wardabbass.redit.ui.adapter

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.wardabbass.redit.R
import com.wardabbass.redit.common.adapters.BaseViewHolder
import com.wardabbass.redit.models.DEFAULT_THUMB
import com.wardabbass.redit.models.ReditPost

class RedditPostViewHolder(view: View) : BaseViewHolder<ReditPost, RedditPostClickListener>(view) {


    val imageView: ImageView = itemView.findViewById(R.id.imageView)
    val titleTextView: TextView = itemView.findViewById(R.id.titleView)
    override fun onBind(item: ReditPost, listener: RedditPostClickListener?) {
        val haveThumbnail = !TextUtils.isEmpty(item.thumbnail) || !DEFAULT_THUMB.equals(item.thumbnail,true)

        imageView.visibility=if(haveThumbnail) View.VISIBLE else View.GONE
        if (haveThumbnail) {
            //load thumbnail
            Glide.with(imageView).load(item.thumbnail)
                    .apply(RequestOptions().centerCrop().placeholder(R.drawable.reddit).diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .transition(DrawableTransitionOptions.withCrossFade(300))
                    .into(imageView)
        }

        titleTextView.text = item.title
        itemView.setOnClickListener {
            listener?.onItemClicked(item,adapterPosition,itemView)
        }
    }

}