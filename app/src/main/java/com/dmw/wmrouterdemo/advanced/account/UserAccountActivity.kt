package com.dmw.wmrouterdemo.advanced.account

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dmw.lib2.ACCOUNT
import com.dmw.lib2.ACCOUNT_1
import com.dmw.lib2.advanced.services.DemoServiceManager
import com.dmw.wmrouterdemo.R
import com.sankuai.waimai.router.annotation.RouterUri
import kotlinx.android.synthetic.main.activity_account.*

@RouterUri(
    path = [ACCOUNT,ACCOUNT_1],//配置多个path
    interceptors = [LoginInterceptor::class]
)
class UserAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "我的账户"
        setContentView(R.layout.activity_account)
        btnLogout.setOnClickListener {
            DemoServiceManager.getAccountService().notifyLogout()
            finish()
        }
    }
}
