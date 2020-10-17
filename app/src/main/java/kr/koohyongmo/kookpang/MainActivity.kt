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