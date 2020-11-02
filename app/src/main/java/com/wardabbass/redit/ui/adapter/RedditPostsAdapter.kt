package com.wardabbass.redit.ui.adapter

import android.view.ViewGroup
import com.wardabbass.redit.common.adapters.BaseAdapter
import com.wardabbass.redit.models.ReditPost
import com.wardabbass.redit.ui.reddit.RedditPostView

class RedditPostsAdapter : BaseAdapter<ReditPost, RedditPostClickListener, RedditPostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditPostViewHolder {

        val reditPostView = RedditPostView(parent.context)
        return RedditPostViewHolder(reditPostView)
    }
}