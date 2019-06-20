package com.dmw.wmrouterdemo.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dmw.lib2.ACCOUNT
import com.dmw.wmrouterdemo.R
import com.sankuai.waimai.router.common.FragmentUriRequest
import com.sankuai.waimai.router.core.OnCompleteListener
import com.sankuai.waimai.router.core.UriRequest
import kotlinx.android.synthetic.main.fragment_demo.*

/**
 * A simple [Fragment] subclass.
 * Use the [DemoFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DemoFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance() = DemoFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_demo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnJump.setOnClickListener {

            FragmentUriRequest(this@DemoFragment, ACCOUNT)
                .activityRequestCode(100)
                .putExtra("test_int", 1)
                .putExtra("test_str", "str")
                .overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity)
                .onComplete(object : OnCompleteListener {
                    override fun onSuccess(request: UriRequest) {
                        Toast.makeText(request.context, "跳转成功", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(request: UriRequest, resultCode: Int) {
                        Toast.makeText(request.context, "跳转失败", Toast.LENGTH_SHORT).show()
                    }
                })
                .start()
        }
    }


}
