package com.wardabbass.redit.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.ward.wrecycler.WardRecycler
import com.wardabbass.redit.R
import com.wardabbass.redit.models.ReditPost
import com.wardabbass.redit.ui.adapter.RedditPostsAdapter
import com.wardabbass.redit.ui.adapter.ReditPostClickListener
import com.wardabbass.redit.viewmodels.TopFeedViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TopFeedFragment : FilteredFragment() {

    companion object {
        const val TAG = "TopFeedFragment"
    }

    private lateinit var wardRecycler: WardRecycler
    private lateinit var topFeedViewModel: TopFeedViewModel
    private lateinit var adapter: RedditPostsAdapter
    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_top_feed, container, false)
        wardRecycler = rootView.findViewById(R.id.pullToLoadView)
        adapter = RedditPostsAdapter()
        wardRecycler.setAdapter(adapter)
        topFeedViewModel = ViewModelProviders.of(this).get(TopFeedViewModel::class.java)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        wardRecycler.onRefresh = {
            handleRefresh()
        }
        wardRecycler.onLoadMore = { currentItemCount, pageSize ->
            handleLoadMore()
        }
        wardRecycler.initLoading()

    }


    private fun initAdapter() {
        adapter.items = topFeedViewModel.reditPosts
        adapter.clickListener = object : ReditPostClickListener {
            override fun onItemClicked(item: ReditPost, position: Int, view: View) {
                redditPostClickDelegate?.onRedditPostClicked(item)
            }

        }
    }

    private fun handleRefresh() {
        loadData(false)
    }


    private fun handleLoadMore() {
        loadData(true)
    }

    private fun loadData(loadMore: Boolean) {
        compositeDisposable.add(topFeedViewModel.fetchFeed(WardRecycler.DEFAULT_PAGE_SIZE, loadMore,
                onComplete = { fromLoadMore, haveMoreToLoad, data ->
                    handleNewData(fromLoadMore, haveMoreToLoad, data)
                }, onError = {
            Log.e(TAG, "error loading data ", it)
        }))
    }

    private fun handleNewData(fromLoadMore: Boolean, haveMoreToLoad: Boolean, data: MutableList<ReditPost>) {

        Log.d(TAG, "handleNewData fromLoadMore:$fromLoadMore, haveMoreToLoad:$haveMoreToLoad ")
        wardRecycler.isLastPage = !haveMoreToLoad
        wardRecycler.completeLoading()

        if (fromLoadMore) {
            adapter.addItems(data)
        } else {
            adapter.items = data
        }

    }

    override fun onQueryChanged() {
        if (query.isNotEmpty()) {
            wardRecycler.disableRefresh()
            wardRecycler.enableLoadMore = false
            makeFilter()
        } else {
            wardRecycler.enableRefresh()
            wardRecycler.enableLoadMore = true
            adapter.items = topFeedViewModel.reditPosts
        }

        Log.d(TAG, "onQueryChanged : $query")
    }

    private fun makeFilter() {
        compositeDisposable.add(Observable.fromIterable(topFeedViewModel.reditPosts)
                .subscribeOn(Schedulers.computation())
                .filter { filter(it) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    topFeedViewModel.filterdPosts.clear()
                    topFeedViewModel.filterdPosts = it
                    adapter.items = topFeedViewModel.filterdPosts

                }, {

                }))

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }


}
