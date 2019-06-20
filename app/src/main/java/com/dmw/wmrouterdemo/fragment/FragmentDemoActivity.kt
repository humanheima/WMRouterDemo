package com.dmw.wmrouterdemo.fragment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dmw.lib2.JUMP_FRAGMENT_ACTIVITY
import com.dmw.wmrouterdemo.R
import com.sankuai.waimai.router.annotation.RouterUri

@RouterUri(
    path = [JUMP_FRAGMENT_ACTIVITY]
)
class FragmentDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_demo)
        launchFragment()
    }

    private fun launchFragment() {
        val fragment = DemoFragment.newInstance()
        supportFragmentManager.beginTransaction().add(
            R.id.flContainer,
            fragment
        ).commit()
    }
}
