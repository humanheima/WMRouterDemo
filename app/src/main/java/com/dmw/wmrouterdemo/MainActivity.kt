package com.dmw.wmrouterdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dmw.wmrouterdemo.inter.ImageLoader
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnUseServiceLoader.setOnClickListener {

            testServiceLoader()
        }

    }

    private fun testServiceLoader() {
        /**
         * 这三种方式都可以使用
         */
        val loader: ServiceLoader<ImageLoader> = ServiceLoader.load(ImageLoader::class.java)
        //val loader: ServiceLoader<ImageLoader> = ServiceLoader.load(ImageLoader::class.java,this.classLoader)
        //val loader: ServiceLoader<ImageLoader> = ServiceLoader.load(ImageLoader::class.java,ImageLoader::class.java.classLoader)
        val it: Iterator<ImageLoader> = loader.iterator()
        val builder = StringBuilder("加载的类：\n")
        while (it.hasNext()) {
            val next = it.next()
            next.loadImg()
            builder.append("${next.javaClass.canonicalName}\n")
        }
        tvLoadedService.text = builder.toString()
    }
}
