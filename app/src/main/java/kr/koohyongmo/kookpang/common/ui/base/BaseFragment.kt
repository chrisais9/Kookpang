package kr.koohyongmo.kookpang.common.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

/**
 * Created by KooHyongMo on 2020/10/11
 */
abstract class BaseFragment : Fragment() {
    /**
     * setContentView로 호출할 Layout의 리소스 ID
     * ex) R.layout.activity_main
     */
    protected abstract val layoutResourceID: Int

    /**
     * setSupportActionBar 로 설정할 Toolbar의 ID
     * ex)
     */
    protected abstract val layoutToolbarID: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(layoutToolbarID != 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResourceID, container, false).apply {
            if (layoutToolbarID != 0) {
                (requireActivity() as AppCompatActivity).setSupportActionBar(
                    findViewById<Toolbar>(layoutToolbarID).apply {
                        title = ""
                    })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayoutAttributes()
    }

    /**
     * 레이아웃을 띄운 직후 호출.
     * 뷰나 액티비티의 속성 등을 초기화.
     * ex) 리사이클러뷰, 툴바, 드로어뷰..
     */
    abstract fun initLayoutAttributes()

}