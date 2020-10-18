package kr.koohyongmo.kookpang.purchase.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_purchase.*
import kr.koohyongmo.kookpang.R
import kr.koohyongmo.kookpang.common.ui.base.BaseActivity

/**
 * 아이템 구매 화면 엑티비티
 *
 * Created by KooHyongMo on 2020/10/18
 */
class PurchaseActivity
    : BaseActivity() {
    override val layoutResourceID: Int
        get() = R.layout.activity_purchase
    override val layoutToolbarID: Int
        get() = R.id.toolbar

    private var productNameList = arrayListOf<String>()
    private var productPriceList = arrayListOf<String>()

    private var isAddressEmpty = true
    private var isPhoneEmpty = true

    override fun initLayoutAttributes() {
        productNameList.addAll(intent.getSerializableExtra("name") as ArrayList<String>)
        productPriceList.addAll(intent.getSerializableExtra("price") as ArrayList<String>)

        initProductListAndSum()
        initUserInfo()
        initButton()
    }

    /**
     * 상품 정보 필드 초기화하는 함수
     * activity intent 로 부터 넘어온 값을 읽어들인후
     *
     * """
     * 1. 에어팟 ₩219,000
     * 2. 퐁퐁 세제 ₩9,800
     * """
     * 같이 만들어줌
     *
     * 그리고 가격의 총합도 업데이트함
     */

    private fun initProductListAndSum() {
        var productInfoString = ""
        var productPriceSum = 0
        productNameList.forEachIndexed { index, _ ->
            productInfoString += "${index + 1}. ${productNameList[index]} ${productPriceList[index]}\n"
            productPriceSum += productPriceList[index].filter { it.isDigit() }.toInt()
        }
        tv_products.text = productInfoString
        tv_sum.text = "총합 : ₩$productPriceSum"
    }

    /**
     * 유저 정보 입력 필드들 초기화하는 함수
     */

    private fun initUserInfo() {

        // 주소 입력 필드
        et_address.addTextChangedListener {
            if (it?.isEmpty()!!) {
                // 주소가 비어있으면 Edittext 밑줄 빨간색
                et_address.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ED4337"))
                isAddressEmpty = true
            } else {
                // 그렇지 않으면 Edittext 밑줄 초록색
                et_address.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#00FF00"))
                isAddressEmpty = false
            }
            setButtonAvailability()
        }

        // 연락처 정보 입력 필드
        et_phone.addTextChangedListener {
            if (it.toString().matches("[0-9]{3}\\-?[0-9]{4}\\-?[0-9]{4}$".toRegex())) {
                // 휴대폰 정보가 01012345678 같이 올바르면 Edittext 밑줄 초록색
                et_phone.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#00FF00"))
                isPhoneEmpty = false
            } else {
                // 그렇지 않으면 Edittext 밑줄 빨간색
                et_phone.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ED4337"))
                isPhoneEmpty = true
            }
            setButtonAvailability()
        }
    }

    /**
     * 버튼이 활성화 / 비활성화 하는 함수
     * 연락처 정보 필드가 올바르며 주소 정보 필드가 올바를때 활성화
     */

    private fun setButtonAvailability() {
        btn_buy.isEnabled = !isAddressEmpty && !isPhoneEmpty
    }

    /**
     * 버튼을 클릭했을때 동작 초기화
     */
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