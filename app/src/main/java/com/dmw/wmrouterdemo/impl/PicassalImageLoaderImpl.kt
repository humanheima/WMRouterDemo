package com.dmw.wmrouterdemo.impl

import android.content.Context
import android.util.Log
import com.dmw.wmrouterdemo.inter.ImageLoader

/**
 * Created by dumingwei on 2019-06-19.
 * Desc:
 */
class PicassalImageLoaderImpl @JvmOverloads constructor(
        var context: Context? = null
) : ImageLoader {

    private val TAG = "PicassalImageLoaderImpl"

    init {
        Log.d(TAG, "init")
    }

    override fun loadImg() {
        Log.d(TAG, "loadImg: ")
    }
}