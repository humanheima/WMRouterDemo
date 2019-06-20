package com.dmw.wmrouterdemo.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.dmw.lib2.*
import com.dmw.lib2.advanced.services.IAccountService
import com.dmw.wmrouterdemo.R
import com.dmw.wmrouterdemo.advanced.account.LoginActivity
import com.dmw.wmrouterdemo.inter.ImageLoader
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.common.DefaultUriRequest
import com.sankuai.waimai.router.core.OnCompleteListener
import com.sankuai.waimai.router.core.UriRequest
import com.sankuai.waimai.router.method.Func2
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnUseServiceLoader.setOnClickListener {
            testServiceLoader()
        }

        btnWMRouterServiceLoader.setOnClickListener {
            val classes = Router.getAllServiceClasses<IAccountService, IAccountService>(IAccountService::class.java)
            val builder = StringBuilder()
            for (clazz in classes) {
                builder.append(clazz.canonicalName)
                builder.append("\n")
            }
            tvLoadedService.text = builder.toString()
        }

        //简单跳转
        btnToAccount.setOnClickListener {
            //Router.startUri(this, ACCOUNT)
            Router.startUri(UriRequest(this, ACCOUNT_1))
        }

        btnRequestJump.setOnClickListener {
            DefaultUriRequest(this, JUMP_WITH_REQUEST)
                .activityRequestCode(100)
                .putExtra(TestUriRequestActivity.INTENT_TEST_INT, 1)
                .putExtra(TestUriRequestActivity.INTENT_TEST_STR, "str")
                .overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity)
                .onComplete(object : OnCompleteListener {
                    override fun onError(request: UriRequest, resultCode: Int) {
                        Toast.makeText(this@MainActivity, "跳转失败", Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccess(request: UriRequest) {
                        Toast.makeText(this@MainActivity, "跳转成功", Toast.LENGTH_SHORT).show()
                    }
                })
                .start()
        }

        btnABTest.setOnClickListener {
            Router.startUri(this, HOME_AB_TEST)
        }

        btnRouterRegex.setOnClickListener {
            Router.startUri(this, MEITUAN_FLUTTER)
            //Router.startUri(this, "https://www.baidu.com")
        }

        btnRouterPage.setOnClickListener {
            Router.startUri(UriRequest(this, INNER_PAGE))
        }

        btnInvokeMethod.setOnClickListener {
            val fun2: Func2<Int, Int, Int> = Router.getService(Func2::class.java, "/add")
            tvResult.text = fun2.call(1, 2).toString()
        }
        btnDowngrade.setOnClickListener {
            //启动一个不存在的uri
            Router.startUri(this, "unkonwn uri")
        }
        //todo 还是有问题
        btnLocalDowngrade.setOnClickListener {
            Router.startUri(UriRequest(this, "/local_downgrade/not_found").onComplete(
                object : OnCompleteListener {
                    override fun onSuccess(request: UriRequest) {
                        Toast.makeText(this@MainActivity, "请求跳转成功", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(request: UriRequest, resultCode: Int) {
                        Toast.makeText(this@MainActivity, "请求跳转失败", Toast.LENGTH_SHORT).show()
                        val intent = Intent(request.context, LoginActivity::class.java)
                        request.context.startActivity(intent)
                    }
                }
            ))
        }

        btnTestFragment.setOnClickListener {
            Router.startUri(this, JUMP_FRAGMENT_ACTIVITY)
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
