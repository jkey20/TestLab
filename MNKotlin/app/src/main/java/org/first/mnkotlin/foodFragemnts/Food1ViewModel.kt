package org.first.mnkotlin.foodFragemnts

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import org.first.mnkotlin.R

class Food1ViewModel : ViewModel() {

    var url = "http://www.pizzabig.co.kr/theme/basic/img/sub0201_img03.jpg"

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