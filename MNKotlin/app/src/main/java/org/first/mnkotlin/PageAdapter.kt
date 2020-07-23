package org.first.mnkotlin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import org.first.mnkotlin.foodFragemnts.*

class PageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {

            0 -> {
                var fragment = Food1Framgent()
                return fragment
            }
            1 -> {
                var fragment = Food2Fragment()
                return fragment
            }
            2 -> {
                var fragment = Food3Fragment()
                return fragment
            }
            3 -> {
                var fragment = Food4Fragment()
                return fragment
            }
            4 -> {
                var fragment = Food5Fragment()
                return fragment
            }
        }
        return Food1Framgent()
    }

    override fun getCount(): Int {
        return 5
    }

}