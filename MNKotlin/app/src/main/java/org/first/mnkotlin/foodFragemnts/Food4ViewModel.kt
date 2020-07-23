package org.first.mnkotlin.foodFragemnts

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide

class Food4ViewModel : ViewModel() {
    var url = "https://w.namu.la/s/8c2aebf04d4c6e0ae24ebf3b3789cb064f353da40f0a2916630ee33cc34742414ac8427b8765569e84d615a24cac7bc389ada2e5c60579541ea8b41be9b22db60f54f1ff9d565415353449e096ffba99c8aaf56bc7b78c1661e8a96757f5703f158a9839035ba4282faf56f4574cb728"
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
