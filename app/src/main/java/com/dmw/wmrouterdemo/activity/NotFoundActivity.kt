package com.dmw.wmrouterdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dmw.wmrouterdemo.R

class NotFoundActivity : AppCompatActivity() {


    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, NotFoundActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "404 not found"
        setContentView(R.layout.activity_not_found)
    }
}
