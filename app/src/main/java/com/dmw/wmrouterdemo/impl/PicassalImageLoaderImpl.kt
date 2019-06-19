package com.dmw.wmrouterdemo.impl

import android.util.Log
import com.dmw.wmrouterdemo.inter.ImageLoader

/**
 * Created by dumingwei on 2019-06-19.
 * Desc:
 */
class PicassalImageLoaderImpl : ImageLoader {

    private val TAG = "PicassalImageLoaderImpl"

    init {
        Log.d(TAG, "init")
    }

    override fun loadImg() {
        Log.d(TAG, "loadImg: ")
    }
}