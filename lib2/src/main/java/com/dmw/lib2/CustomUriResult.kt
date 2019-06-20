package com.dmw.lib2

/**
 * 自定义的UriResultCode，为了避免冲突，自定义的建议用负数值
 *
 * Created by jzj on 2018/3/27.
 */
interface CustomUriResult {

    companion object {

        val CODE_LOGIN_CANCEL = -100
        val CODE_LOGIN_FAILURE = -101

        val CODE_LOCATION_FAILURE = -200
    }
}
