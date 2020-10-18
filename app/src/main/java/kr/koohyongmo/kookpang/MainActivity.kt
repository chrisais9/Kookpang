package kr.koohyongmo.kookpang

import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kr.koohyongmo.kookpang.common.ui.base.BaseActivity
import kr.koohyongmo.kookpang.common.ui.viewpager.HomeModeViewPager

class MainActivity : BaseActivity() {
    override val layoutResourceID: Int
        get() = R.layout.activity_main
    override val layoutToolbarID: Int
        get() = R.id.toolbar

    override val enableBackButtonOnToolbar: Boolean = false

    private val modeList by lazy {
        arrayListOf(
            getString(R.string.tab_home),
            getString(R.string.tab_shopping_list)
        )
    }

    override fun initLayoutAttributes() {
        initializeTab()
    }

    /**
     * 홈, 장바구니 탭 구성
     */
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