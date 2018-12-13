package com.ward.wrecycler

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.jetbrains.anko.dip

open class WardRecycler @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, theme: Int = -1) : FrameLayout(context, attributeSet, theme) {

    companion object {
        const val DEFAULT_PAGE_SIZE = 25
    }

    private var isLoading = false
        set(value) {
            field = value
            swipeRefreshLayout.isEnabled = !field
        }

    var enableLoadMore = true

    var isLastPage = false

    var progressColor = Color.DKGRAY
        set(value) {
            field = value
            updateProgressColor()
        }

    var progressBarSize: Int = 0
    var itemSpacing: Int = 0
    var pageSize = DEFAULT_PAGE_SIZE
    /**
     * callback whenever refresh called
     */
    var onRefresh: () -> Unit = {}

    var onLoadMore: (currentItemCount: Int, pageSize: Int) -> Unit = { _, _ ->
    }

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var progressView: ProgressBar


    var layoutManager: LinearLayoutManager = LinearLayoutManager(context)
        set(value) {
            field = value
            value.isItemPrefetchEnabled = false
            recyclerView.layoutManager = value
        }

    lateinit var recyclerView: RecyclerView
        protected set

    private val pagingScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItems = layoutManager.childCount
            val itemsCount = layoutManager.itemCount

            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if (!isLoading && !isLastPage) {

                if ((visibleItems + firstVisibleItemPosition) >= itemsCount && firstVisibleItemPosition >= 0
                        && itemsCount >= pageSize) {
                    if (enableLoadMore) {
                        isLoading = true
                        onLoadMore(itemsCount, pageSize)
                        showProgressBar()
                    }
                }
            }

        }
    }

    private fun showProgressBar() {

        progressView.visibility = View.VISIBLE
        recyclerView.invalidateItemDecorations()

    }

    init {
        initViews()
        initListeners()
    }

    private fun initViews() {
        View.inflate(context, R.layout.pull_to_load_view, this)
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val pos = parent.getChildAdapterPosition(view)

                outRect.left = itemSpacing
                outRect.right = itemSpacing
                if (progressView.visibility == View.VISIBLE
                        && pos != RecyclerView.NO_POSITION
                        && recyclerView.adapter != null
                        && pos == recyclerView.adapter?.itemCount?.minus(1) ?: 0) {
                    outRect.bottom = progressBarSize + dip(4)
                } else {
                    outRect.bottom = itemSpacing

                }
            }

        })
        initProgressView()
        progressBarSize = resources.getDimension(R.dimen.recycler_view_progress_size).toInt()
    }

    private fun initProgressView() {
        progressView = findViewById(R.id.progressBar)
        updateProgressColor()
    }

    private fun updateProgressColor() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val wrapDrawable = DrawableCompat.wrap(progressView.indeterminateDrawable)
            DrawableCompat.setTint(wrapDrawable, progressColor)
            progressView.indeterminateDrawable = DrawableCompat.unwrap<Drawable>(wrapDrawable)
        } else {
            progressView.indeterminateDrawable.setColorFilter(progressColor, PorterDuff.Mode.MULTIPLY)
        }
    }


    private fun initListeners() {
        swipeRefreshLayout.setOnRefreshListener {
            isLoading = true
            onRefresh()
        }

        recyclerView.addOnScrollListener(pagingScrollListener)
    }

    fun disableRefresh() {
        swipeRefreshLayout.isEnabled = false
    }

    fun enableRefresh() {
        swipeRefreshLayout.isEnabled = true
    }

    /**
     * makes first loading ,sets islastpage to false user responsible for updating this value
     */
    fun initLoading() {
        isLastPage = false
        isLoading = true
        onRefresh()
        swipeRefreshLayout.isRefreshing = true
    }

    fun completeLoading() {
        isLoading = false
        swipeRefreshLayout.isRefreshing = false
        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
             TransitionManager.beginDelayedTransition(this, AutoTransition())
         }*/
        progressView.visibility = GONE
        recyclerView.invalidateItemDecorations()
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>) {

        this.recyclerView.adapter = adapter
    }

    fun setColorSchemeResources(@ColorInt vararg colors: Int) {
        swipeRefreshLayout.setColorSchemeColors(*colors)
    }

}