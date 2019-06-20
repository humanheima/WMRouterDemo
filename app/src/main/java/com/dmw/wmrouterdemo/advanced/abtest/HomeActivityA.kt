package com.dmw.wmrouterdemo.advanced.abtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dmw.wmrouterdemo.R

class HomeActivityA : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "HomeActivityA"
        setContentView(R.layout.activity_home_activity)
    }
}
