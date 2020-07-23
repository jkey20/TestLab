package org.first.mnkotlin

import android.app.ProgressDialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_glide_view_pager.*
import org.first.mnkotlin.databinding.ActivityGlideViewPagerBinding
import org.first.mnkotlin.foodFragemnts.*

class GlideViewPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGlideViewPagerBinding
    private var pageAdapter =  PageAdapter(supportFragmentManager)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide_view_pager)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_glide_view_pager)
        binding.viewpager.adapter = pageAdapter
    }
}
