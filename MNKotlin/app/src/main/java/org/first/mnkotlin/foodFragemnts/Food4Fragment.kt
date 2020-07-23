package org.first.mnkotlin.foodFragemnts

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import org.first.mnkotlin.R
import org.first.mnkotlin.databinding.FragmentFood3Binding
import org.first.mnkotlin.databinding.FragmentFood4Binding

class Food4Fragment : Fragment() {

    private lateinit var binding: FragmentFood4Binding
    private lateinit var viewmodel : Food4ViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_food4, container, false)
        viewmodel = ViewModelProviders.of(this).get(Food4ViewModel::class.java)
        binding.vm = Food4ViewModel()
        return binding.root
    }

}
