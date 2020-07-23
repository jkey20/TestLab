package org.first.mnkotlin.foodFragemnts

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import org.first.mnkotlin.PageAdapter
import org.first.mnkotlin.R
import org.first.mnkotlin.RestaurantListActivity
import org.first.mnkotlin.databinding.FragmentFood5Binding

class Food5Fragment : Fragment() {

    private lateinit var binding: FragmentFood5Binding
    private lateinit var viewmodel : Food5ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_food5, container, false)
        viewmodel = ViewModelProviders.of(this).get(Food5ViewModel::class.java)
        binding.vm = Food5ViewModel()

        if(viewmodel.isOpenDialog){
            useHandler()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        useHandler()
    }

    override fun onPause() {
        super.onPause()
        viewmodel.isOpenDialog = true;
    }
    fun showDialog(){
        var builder = AlertDialog.Builder(context)
        builder.setTitle("주변 음식점 찾기")
        builder.setMessage("주변에 음식점을 찾고 \n" +
                "같이 먹을 사람을 찾아보시겠어요?")
        builder.setCancelable(true)
        builder.setPositiveButton("네!") { dialog, id ->
            var intent = Intent(activity, RestaurantListActivity::class.java)
            startActivity(intent)
        }
        builder.setNegativeButton("아니요"){ dialog, id ->
            findNavController().navigate(R.id.action_food5Fragment_to_selectPictureFragment)
        }
        builder.create().show()
    }

    fun useHandler(){
        var handler = Handler()
        handler.postDelayed(object : Runnable{
            override fun run() {
                showDialog()
            }

        }, 1000)
    }
}
