package kr.koohyongmo.kookpang.store.ui

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nitrico.lastadapter.LastAdapter
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_store.*
import kr.koohyongmo.kookpang.BR
import kr.koohyongmo.kookpang.MainActivity
import kr.koohyongmo.kookpang.R
import kr.koohyongmo.kookpang.common.data.model.Product
import kr.koohyongmo.kookpang.common.ui.base.BaseFragment
import kr.koohyongmo.kookpang.databinding.ItemStoreProductBinding
import kr.koohyongmo.kookpang.store.viewmodel.ProductStoreViewModel

/**
 * Created by KooHyongMo on 2020/10/11
 */

class StoreFragment
    : BaseFragment() {

    override val layoutResourceID: Int
        get() = R.layout.fragment_store
    override val layoutToolbarID: Int
        get() = 0

    private val productListData = arrayListOf<ProductStoreViewModel>()
    val productListAdapter = LastAdapter(productListData, BR.listContent)

    private var selectedProduct: ProductStoreViewModel? = null

    /**
     * Fragment가 user에게 보이거나 사라질떄 호출되는 함수
     */
    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        // 장바구니로 이동했다가 다시 돌아왔을때 버튼 없애주기 위해서
        onClickProduct(null)
    }

    override fun initLayoutAttributes() {
        initRecyclerView()
        initProductItems()
        initButton()
    }

    private fun initProductItems() {
        productListData.add(
            ProductStoreViewModel(
                "https://img.danawa.com/prod_img/500000/890/719/img/2719890_1.jpg?shrink=500:500&_v=20171122173134",
                "퐁퐁 세제",
                "₩9,800"
            )
        )

        productListData.add(
            ProductStoreViewModel(
                "https://imgc.1300k.com/aaaaaib/goods/215024/96/215024967618.jpg?3",
                "에어팟",
                "₩219,000"
            )
        )
        productListAdapter.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        rv_list.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        productListAdapter
            .map<ProductStoreViewModel, ItemStoreProductBinding>(R.layout.item_store_product) {
                onClick {
                    onClickProduct(it.binding.listContent)
                }
            }
            .into(rv_list)
    }

    private fun initButton() {
        btn_shopping_list.setOnClickListener {
            selectedProduct?.let { product ->
                onSaveToShoppingList(product)
            }
        }


    }

    /**
     * 1. 리스트내 아이템을 클릭했을때 호출된다.
     * 2. 장바구니로 이동했다가 다시 홈으로 돌아왔을때 호출된다.
     *
     * 처음에는 버튼이 보이지 않다가 아이템을 클릭하면 버튼이 보이고 동일한 아이템을
     * 클릭하면 버튼이 사라진다.
     * 구매, 호출 로직을 위해 선택된 아이템은 [selectedProduct] 에 저장된다
     */
    private fun onClickProduct(product: ProductStoreViewModel?) {
        if(product == null) {
            btn_buy.visibility = View.GONE
            btn_shopping_list.visibility = View.GONE
            selectedProduct = null
        } else if (selectedProduct == null) {
            btn_buy.visibility = View.VISIBLE
            btn_shopping_list.visibility = View.VISIBLE
            selectedProduct = product
        } else if (selectedProduct == product) {
            btn_buy.visibility = View.GONE
            btn_shopping_list.visibility = View.GONE
            selectedProduct = null
        } else {
            selectedProduct = product
        }
    }

    private fun onSaveToShoppingList(productToAdd: ProductStoreViewModel) {
        val realm = Realm.getDefaultInstance()

        // 아래 부터는 장바구니에 아이템들을 담기위해 DB에 데이터를 추가 하는 과정
        realm.executeTransaction {
            it.where(Product::class.java)
                .equalTo("preview", productToAdd.preview)
                .equalTo("name", productToAdd.name)
                .equalTo("price", productToAdd.price)
                .findFirst()
            // 동일한 객체를 DB 에서 찾고
                ?: run {
                    // 동일한 객체가 없으면(null 이면) 넣어줌
                    val product = realm.createObject(Product::class.java)
                    product.apply {
                        preview = productToAdd.preview
                        name = productToAdd.name
                        price = productToAdd.price
                    }
                    it.insertOrUpdate(product)
                }
        }
        realm.close()
        // 장바구니로 뷰 포커스 이동
        (activity as MainActivity).vp_mode.setCurrentItem(1, true)
    }
}