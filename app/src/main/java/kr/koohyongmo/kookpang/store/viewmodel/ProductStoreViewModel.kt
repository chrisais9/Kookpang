package kr.koohyongmo.kookpang.store.viewmodel

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.realm.Realm
import kr.koohyongmo.kookpang.common.data.model.Product

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

    fun onSaveToShoppingList(view: View) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.where(Product::class.java)
                .equalTo("preview", preview)
                .equalTo("name", name)
                .equalTo("price", price)
                .findFirst()
                ?: run {
                    // if non null then add products to DB
                    val product = realm.createObject(Product::class.java)
                    product.apply {
                        preview = this@ProductStoreViewModel.preview
                        name = this@ProductStoreViewModel.name
                        price = this@ProductStoreViewModel.price
                    }
                    it.insertOrUpdate(product)
                }
        }
        realm.close()
    }

}