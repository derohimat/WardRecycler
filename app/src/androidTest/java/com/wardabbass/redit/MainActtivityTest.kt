package com.wardabbass.redit

import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import androidx.viewpager.widget.ViewPager
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActtivityTest {
    @get:Rule
    val mainActivityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun tabsTest() {
        Espresso.onView(withId(R.id.feed)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.fav)).check(matches(isDisplayed()))
        val bottomNavigationView = mainActivityTestRule.activity.findViewById<BottomNavigationView>(R.id.navigationView)
        val viewPager = mainActivityTestRule.activity.findViewById<androidx.viewpager.widget.ViewPager>(R.id.viewPager)
        assertTrue(bottomNavigationView.menu.findItem(R.id.feed).isChecked)
        assertTrue(viewPager.currentItem == 0)

    }
}