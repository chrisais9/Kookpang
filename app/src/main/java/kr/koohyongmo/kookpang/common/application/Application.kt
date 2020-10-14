package kr.koohyongmo.kookpang.common.application

import android.app.Application
import io.realm.Realm

/**
 * Created by KooHyongMo on 2020/10/15
 */
class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}