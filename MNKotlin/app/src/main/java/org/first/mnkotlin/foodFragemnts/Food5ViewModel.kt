package org.first.mnkotlin.foodFragemnts

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide

class Food5ViewModel : ViewModel() {
    var isOpenDialog = false
    var url = "https://cdn.dominos.co.kr/admin/upload/goods/20200119_Dzj9GV1R.jpg"
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
