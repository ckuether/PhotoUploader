package com.example.photouploader

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PhotoUploaderCallback {

    private lateinit var adapterViewPager: MyPagerAdapter

    var savedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapterViewPager = MyPagerAdapter(supportFragmentManager)
        viewpager.adapter = adapterViewPager
        viewpager.currentItem = 1
    }

    override fun updateSavedBitmap(bitmap: Bitmap?) {
        savedBitmap = bitmap
    }

    inner class MyPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    ChatFragment.newInstance()
                }
                1 -> {
                    CameraFragment.newInstance(this@MainActivity)
                }
                2 -> {
                    StoryFragment.newInstance()
                }
                else -> {
                    ChatFragment.newInstance()
                }
            }
        }

        override fun getCount(): Int {
            return 3
        }

    }
}
