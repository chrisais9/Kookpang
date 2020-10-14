package kr.koohyongmo.kookpang.shoppinglist.ui

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nitrico.lastadapter.LastAdapter
import io.realm.Realm
import io.realm.RealmChangeListener
import kotlinx.android.synthetic.main.fragment_shopping_list.*
import kotlinx.android.synthetic.main.fragment_shopping_list.rv_list
import kr.koohyongmo.kookpang.BR
import kr.koohyongmo.kookpang.R
import kr.koohyongmo.kookpang.common.data.model.Product
import kr.koohyongmo.kookpang.common.ui.base.BaseFragment
import kr.koohyongmo.kookpang.databinding.ItemShoppingListProductBinding
import kr.koohyongmo.kookpang.shoppinglist.viewmodel.ProductShoppingListViewModel

/**
 * Created by KooHyongMo on 2020/10/11
 */

class ShoppingListFragment
    : BaseFragment() {

    override val layoutResourceID: Int
        get() = R.layout.fragment_shopping_list
    override val layoutToolbarID: Int
        get() = 0

    private var productListData = arrayListOf<ProductShoppingListViewModel>()
    private val productListAdapter = LastAdapter(productListData, BR.listContent)


    override fun initLayoutAttributes() {
        tv_empty.text = "장바구니가 비어 있어요~~"
        initRecyclerView()
        initSavedItems()
    }

    private fun initRecyclerView() {
        rv_list.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        productListAdapter
            .map<ProductShoppingListViewModel, ItemShoppingListProductBinding>(R.layout.item_shopping_list_product)
            .into(rv_list)
    }

    private fun initSavedItems() {
        val realm = Realm.getDefaultInstance()
        val items = realm.where(Product::class.java)
            .findAllAsync()
        items.addChangeListener { product, _ ->
            productListData.clear()
            productListData.addAll(
                product.map { ProductShoppingListViewModel(it.preview, it.name, it.price) }
            )
            tv_empty.visibility = if (productListData.isNotEmpty()) View.GONE else View.VISIBLE
            productListAdapter.notifyDataSetChanged()
        }
    }

}