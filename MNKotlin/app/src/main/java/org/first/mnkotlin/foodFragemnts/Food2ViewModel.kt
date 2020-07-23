package org.first.mnkotlin.foodFragemnts

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide

class Food2ViewModel : ViewModel() {
    var url = "https://www.pizzaetang.com/resources/images/menu/menuinfo/PZ_TT2_l.png"
    object Mybind {
        @JvmStatic
        @BindingAdapter("setImage")
        fun setImageUrl(view: ImageView, image: String) {

            Glide.with(view.context)
                .load(image)
                .into(view)

        }
    }
}
