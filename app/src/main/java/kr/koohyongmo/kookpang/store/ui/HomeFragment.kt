package kr.koohyongmo.kookpang.store.ui

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nitrico.lastadapter.LastAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_store.*
import kr.koohyongmo.kookpang.BR
import kr.koohyongmo.kookpang.MainActivity
import kr.koohyongmo.kookpang.R
import kr.koohyongmo.kookpang.common.data.model.Product
import kr.koohyongmo.kookpang.common.ui.base.BaseFragment
import kr.koohyongmo.kookpang.databinding.ItemStoreProductBinding
import kr.koohyongmo.kookpang.purchase.ui.PurchaseActivity
import kr.koohyongmo.kookpang.store.viewmodel.ProductStoreViewModel

/**
 * 홈 화면 Fragment
 * 상품 리스트가 있으며, 상품 장바구니 추가 및 구매를 진행 할 수 있음
 *
 * Created by KooHyongMo on 2020/10/11
 */

class HomeFragment
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
        Firebase.database.getReferenceFromUrl("https://kookpang-853e8.firebaseio.com/")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    productListData.clear()
                    val products = snapshot.child("products").children.map {
                        it.getValue(ProductStoreViewModel::class.java)!!
                    }
                    productListData.addAll(products)
                    productListAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        "인터넷 연결을 확인하세요.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
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
        btn_buy.setOnClickListener {
            selectedProduct?.let { product ->
                onPurchaseProduct(product)
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
        if (product == null) {
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

    private fun onPurchaseProduct(product: ProductStoreViewModel) {
        startActivity(
            Intent(requireContext(), PurchaseActivity::class.java)
                .putExtra("name", arrayListOf(product.name))
                .putExtra("price", arrayListOf(product.price))
        )
    }
}