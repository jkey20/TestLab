package org.first.mnkotlin.foodFragemnts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide

import org.first.mnkotlin.R
import org.first.mnkotlin.databinding.FragmentFood1Binding

/**
 * A simple [Fragment] subclass.
 */
class Food1Framgent : Fragment() {
    private lateinit var binding: FragmentFood1Binding
    private lateinit var viewmodel : Food1ViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_food1, container, false)
        viewmodel = ViewModelProviders.of(this).get(Food1ViewModel::class.java)
        binding.vm = Food1ViewModel()
        return binding.root
    }

}
