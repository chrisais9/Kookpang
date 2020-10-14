package kr.koohyongmo.kookpang.store.ui

import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.fragment_store.*
import kr.koohyongmo.kookpang.BR
import kr.koohyongmo.kookpang.R
import kr.koohyongmo.kookpang.common.ui.base.BaseFragment
import kr.koohyongmo.kookpang.databinding.ItemProductBinding
import kr.koohyongmo.kookpang.store.ui.viewmodel.ProductViewModel

/**
 * Created by KooHyongMo on 2020/10/11
 */

class StoreFragment
    : BaseFragment() {

    override val layoutResourceID: Int
        get() = R.layout.fragment_store
    override val layoutToolbarID: Int
        get() = 0

    private val productListData = arrayListOf<ProductViewModel>()

    override fun initLayoutAttributes() {
        productListData.add(ProductViewModel(
            "https://img.danawa.com/prod_img/500000/890/719/img/2719890_1.jpg?shrink=500:500&_v=20171122173134",
        "퐁퐁 세제",
        "₩9,800"))

        productListData.add(ProductViewModel(
            "https://imgc.1300k.com/aaaaaib/goods/215024/96/215024967618.jpg?3",
            "에어팟",
            "₩219,000"))

        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv_list.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        LastAdapter(productListData, BR.listContent)
            .map<ProductViewModel, ItemProductBinding>(R.layout.item_product)
            .into(rv_list)
    }
}