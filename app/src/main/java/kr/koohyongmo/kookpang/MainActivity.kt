package kr.koohyongmo.kookpang

import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_store.*
import kr.koohyongmo.kookpang.common.ui.base.BaseActivity
import kr.koohyongmo.kookpang.common.ui.viewpager.HomeModeViewPager
import kr.koohyongmo.kookpang.store.ui.StoreFragment

class MainActivity : BaseActivity() {
    override val layoutResourceID: Int
        get() = R.layout.activity_main
    override val layoutToolbarID: Int
        get() = R.id.toolbar

    override val enableBackButtonOnToolbar: Boolean = false

    val modeList = arrayListOf("스토어", "장바구니")

    override fun initLayoutAttributes() {
        initializeTab()
    }

    private fun initializeTab() {
        vp_mode.adapter = HomeModeViewPager(
            supportFragmentManager,
            lifecycle,
            modeList
        )
        TabLayoutMediator(tl_mode, vp_mode) { tab, position ->
            tab.text = modeList[position]
            vp_mode.setCurrentItem(tab.position, true)
        }.attach()
    }
}