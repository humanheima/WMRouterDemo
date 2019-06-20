package com.dmw.wmrouterdemo.advanced.account

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.dmw.lib2.LOGIN
import com.dmw.lib2.advanced.services.DemoServiceManager
import com.dmw.wmrouterdemo.R
import com.sankuai.waimai.router.annotation.RouterUri
import kotlinx.android.synthetic.main.activity_login.*

@RouterUri(
    path = [LOGIN]
)
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        button.setOnClickListener {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
            DemoServiceManager.getAccountService().notifyLoginSuccess()
            finish()
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "登录取消", Toast.LENGTH_SHORT).show()
        DemoServiceManager.getAccountService().notifyLoginCancel()
        super.onBackPressed()

    }
}
