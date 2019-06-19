package com.dmw.wmrouterdemo.impl

import android.content.Context
import android.util.Log
import com.dmw.wmrouterdemo.inter.ImageLoader

/**
 * Created by dumingwei on 2019-06-19.
 * Desc:
 */
class GlideImageLoaderImpl @JvmOverloads constructor(
        var context: Context? = null
) : ImageLoader {

    private val TAG = "GlideImageLoaderImpl"

    init {
        Log.d(TAG, "init")
    }

    override fun loadImg() {
        Log.d(TAG, "loadImg: ")
    }
}