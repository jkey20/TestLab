package org.first.mnkotlin.foodFragemnts

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide

class Food3ViewModel : ViewModel() {
    var url = "https://image.chosun.com/sitedata/image/201911/11/2019111100790_0.jpg"
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
