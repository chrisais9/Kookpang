package kr.koohyongmo.kookpang

import androidx.fragment.app.Fragment
import kr.koohyongmo.kookpang.common.ui.base.BaseActivity
import kr.koohyongmo.kookpang.home.ui.ProductListFragment

class MainActivity : BaseActivity() {
    override val layoutResourceID: Int
        get() = R.layout.activity_main
    override val layoutToolbarID: Int
        get() = R.id.toolbar

    override val enableBackButtonOnToolbar: Boolean = false

    override fun initLayoutAttributes() {
        setCurrentFragmentTab(ProductListFragment())
    }

    private fun setCurrentFragmentTab(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_fragment_container, fragment)
            .commitAllowingStateLoss()
    }
}