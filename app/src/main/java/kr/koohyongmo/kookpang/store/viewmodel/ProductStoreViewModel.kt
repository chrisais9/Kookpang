package kr.koohyongmo.kookpang.store.viewmodel

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 * Created by KooHyongMo on 2020/10/11
 */

class ProductStoreViewModel(
    val preview: String,
    val name: String,
    val price: String
) {

    companion object {

        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(view: ImageView, url: String?) {
            Glide.with(view.context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view)
        }
    }

}