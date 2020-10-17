package kr.koohyongmo.kookpang.common.data.model

import io.realm.RealmObject

/**
 * Created by KooHyongMo on 2020/10/11
 */
open class Product : RealmObject() {
    var preview: String = ""
    var name: String = ""
    var price: String = ""
}