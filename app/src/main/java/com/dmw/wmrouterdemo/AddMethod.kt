package com.dmw.wmrouterdemo

import com.sankuai.waimai.router.annotation.RouterService
import com.sankuai.waimai.router.method.Func2

/**
 * Created by dumingwei on 2019-06-20.
 * Desc:
 */
@RouterService(
    interfaces = [Func2::class],
    key = ["/add"],
    singleton = true
)
class AddMethod : Func2<Int, Int, Int> {

    override fun call(t1: Int, t2: Int): Int {
        return t1 + t2
    }
}