package com.dmw.wmrouterdemo.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dmw.lib2.JUMP_WITH_REQUEST
import com.dmw.wmrouterdemo.R
import com.sankuai.waimai.router.annotation.RouterUri
import kotlinx.android.synthetic.main.activity_test_uri_request.*

@RouterUri(path = [JUMP_WITH_REQUEST])
class TestUriRequestActivity : AppCompatActivity() {


    companion object {
        const val INTENT_TEST_INT = "test_int"
        const val INTENT_TEST_STR = "test_str"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_uri_request)
        val url = intent.data
        val s = StringBuilder()

        s.append("path:${url.path}\n")
        s.append("schema:${url.scheme}\n")
        s.append("host:${url.host}\n")

        val extras = intent.extras
        if (extras != null) {
            for (key in extras.keySet()) {
                s.append(key).append(" = ").append(extras.get(key)).append('\n')
            }
        }
        tvText.text = s.toString()

    }
}
