package kr.koohyongmo.kookpang.shoppinglist.ui

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.nitrico.lastadapter.LastAdapter
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_shopping_list.*
import kotlinx.android.synthetic.main.item_shopping_list_product.view.*
import kr.koohyongmo.kookpang.BR
import kr.koohyongmo.kookpang.MainActivity
import kr.koohyongmo.kookpang.R
import kr.koohyongmo.kookpang.common.data.model.Product
import kr.koohyongmo.kookpang.common.ui.base.BaseFragment
import kr.koohyongmo.kookpang.databinding.ItemShoppingListProductBinding
import kr.koohyongmo.kookpang.purchase.ui.PurchaseActivity
import kr.koohyongmo.kookpang.shoppinglist.viewmodel.ProductShoppingListViewModel


/**
 * 장바구니 화면 Fragment
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

    private var selectedProduct = arrayListOf<ProductShoppingListViewModel>()


    override fun initLayoutAttributes() {
        tv_empty.text = "장바구니가 비어 있어요~~" // 만약 장바구니에 아이템이 없으면 보이는 텍스트
        initRecyclerView()
        initSavedItems()
        initButtons()
    }

    /**
     * 아이템 리스트 초기화
     */
    private fun initRecyclerView() {
        rv_list.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        productListAdapter
            .map<ProductShoppingListViewModel, ItemShoppingListProductBinding>(R.layout.item_shopping_list_product) {
                onClick {
                    // 아이템 클릭하면 체크박스 Toggle
                    it.itemView.cb_select.isChecked = !it.itemView.cb_select.isChecked

                    // 선택된 아이템 리스트에 추가 / 제거
                    if (it.itemView.cb_select.isChecked) {
                        selectedProduct.add(it.binding.listContent!!)
                    } else {
                        selectedProduct.remove(it.binding.listContent!!)
                    }
                    // 몇개의 아이템이 선택되었는지 표시
                    tv_selected_num.text = "${selectedProduct.size}개 선택됨"

                    // 선택된 아이템이 비어있지 않으면 "구매하기" 버튼 활성화
                    btn_buy.isEnabled = selectedProduct.isNotEmpty()
                }
            }
            .into(rv_list)
    }

    /**
     * Realm DB 로부터 장바구니에 저장된 아이템을 불러와줌
     */
    private fun initSavedItems() {
        val realm = Realm.getDefaultInstance()
        val items = realm.where(Product::class.java)
            .findAllAsync()
        items.addChangeListener { product, _ ->
            productListData.clear()
            selectedProduct.clear()
            productListData.addAll(
                product.map { ProductShoppingListViewModel(it.preview, it.name, it.price, false) }
            )
            tv_empty.visibility = if (productListData.isNotEmpty()) View.GONE else View.VISIBLE
            productListAdapter.notifyDataSetChanged()
        }
    }

    private fun initButtons() {
        btn_buy.setOnClickListener {
            startActivity(
                Intent(requireContext(), PurchaseActivity::class.java)
                    .putExtra("name", ArrayList(selectedProduct.map { it.name }))
                    .putExtra("price", ArrayList(selectedProduct.map { it.price }))
            )
        }

        btn_home.setOnClickListener {
            (activity as MainActivity).vp_mode.setCurrentItem(0, true)
        }
    }
}