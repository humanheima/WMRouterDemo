package com.dmw.wmrouterdemo.advanced.abtest

/**
 * Created by dumingwei on 2019-06-20.
 * Desc:
 */
class FakeABTestService {


    companion object {

        @JvmStatic
        fun getHomeABStrategy(): String {
            return if (Math.random() > 0.5f) {
                "A"
            } else {
                "B"
            }
        }
    }
}