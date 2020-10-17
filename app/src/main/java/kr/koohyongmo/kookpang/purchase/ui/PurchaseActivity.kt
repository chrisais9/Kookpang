package kr.koohyongmo.kookpang.purchase.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_purchase.*
import kr.koohyongmo.kookpang.R
import kr.koohyongmo.kookpang.common.ui.base.BaseActivity

/**
 * Created by KooHyongMo on 2020/10/18
 */
class PurchaseActivity
    : BaseActivity() {
    override val layoutResourceID: Int
        get() = R.layout.activity_purchase
    override val layoutToolbarID: Int
        get() = R.id.toolbar

    var productNameList = arrayListOf<String>()
    var productPriceList = arrayListOf<String>()

    var isAddressEmpty = true
    var isPhoneEmpty = true

    override fun initLayoutAttributes() {
        productNameList.addAll(intent.getSerializableExtra("name") as ArrayList<String>)
        productPriceList.addAll(intent.getSerializableExtra("price") as ArrayList<String>)

        initProductListAndSum()
        initUserInfo()
        initButton()
    }

    private fun initProductListAndSum() {
        var productInfoString = ""
        var productPriceSum = 0
        productNameList.forEachIndexed { index, name ->
            productInfoString += "${index + 1}. ${productNameList[index]} ${productPriceList[index]}\n"
            productPriceSum += productPriceList[index].filter { it.isDigit() }.toInt()
        }
        tv_products.text = productInfoString
        tv_sum.text = "총합 : ₩$productPriceSum"
    }

    private fun initUserInfo() {
        et_address.addTextChangedListener {
            if (it?.isEmpty()!!) {
                et_address.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ED4337"))
                isAddressEmpty = true
            } else {
                et_address.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#00FF00"))
                isAddressEmpty = false
            }
            setButtonAvailability()
        }
        et_phone.addTextChangedListener {
            if (it.toString().matches("[0-9]{3}\\-?[0-9]{4}\\-?[0-9]{4}$".toRegex())) {
                et_phone.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#00FF00"))
                isPhoneEmpty = false
            } else {
                et_phone.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ED4337"))
                isPhoneEmpty = true
            }
            setButtonAvailability()
        }
    }

    private fun setButtonAvailability() {
        btn_buy.isEnabled = !isAddressEmpty && !isPhoneEmpty
    }

    private fun initButton() {
        btn_buy.setOnClickListener {
            if (btn_buy.isEnabled) {
                Toast.makeText(this, "구매가 완료 되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "구매자 정보를 채워주세요!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}