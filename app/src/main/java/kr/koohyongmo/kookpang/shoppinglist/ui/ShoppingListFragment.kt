package kr.koohyongmo.kookpang.shoppinglist.ui

import kotlinx.android.synthetic.main.fragment_shopping_list.*
import kr.koohyongmo.kookpang.R
import kr.koohyongmo.kookpang.common.ui.base.BaseFragment
import kr.koohyongmo.kookpang.store.ui.StoreFragment

/**
 * Created by KooHyongMo on 2020/10/11
 */

class ShoppingListFragment
    : BaseFragment() {

    override val layoutResourceID: Int
        get() = R.layout.fragment_shopping_list
    override val layoutToolbarID: Int
        get() = 0

    override fun initLayoutAttributes() {
        tv_empty.text = "장바구니가 비어 있어요~~"
    }

}