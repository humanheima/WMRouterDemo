package com.dmw.wmrouterdemo.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dmw.lib2.INNER_PAGE
import com.dmw.wmrouterdemo.R
import com.sankuai.waimai.router.annotation.RouterPage

@RouterPage(
    path = [INNER_PAGE]
)
class TestRouterPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "测试RouterPage注解"
        setContentView(R.layout.activity_test_router_page)
    }
}
