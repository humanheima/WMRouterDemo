package com.dmw.wmrouterdemo.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dmw.wmrouterdemo.R
import com.sankuai.waimai.router.annotation.RouterUri

@RouterUri(
    path = ["/TestBasicActivity"]
)
class TestBasicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Basic activity"
        setContentView(R.layout.activity_test_basic)
    }
}
