package kr.koohyongmo.kookpang.common.ui.viewpager

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.koohyongmo.kookpang.shoppinglist.ui.ShoppingListFragment
import kr.koohyongmo.kookpang.store.ui.StoreFragment

/**
 * Created by KooHyongMo on 2020/10/11
 */

class HomeModeViewPager (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val list:ArrayList<String>
): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = list.size

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> StoreFragment()
            1 -> ShoppingListFragment()
            else -> throw Exception("wrong index")
        }

    }
}