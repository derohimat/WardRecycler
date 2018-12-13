package com.wardabbass.redit.ui.viewpager

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * this will  make a cache for fragments, to avoid cases when the app got killed and the fragment state saved by the viewpager
 * template class type of fragments
 */
class ReditFragmentAdapter<out F : Fragment>(fragmentManager: androidx.fragment.app.FragmentManager, private var fragments: Array<F>) : androidx.fragment.app.FragmentStatePagerAdapter(fragmentManager) {

    /**
     * using sparse array for mapping primitve -> fragment
     */
    private val cachedFragments = SparseArray<F>()


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position)  // this will call get item after all
        cachedFragments.put(position, fragment as F)
        return fragment
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        cachedFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    /**
     * dont use it to get fragments by pos use the cached version
     */
    override fun getItem(position: Int): F {
        return fragments[position]
    }

    /**
     * after the app killed we can get the fragment (cached) when instantiateItem called and then return it
     */
    fun getCachedItem(position: Int): F {
        return cachedFragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

}