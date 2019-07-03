SPI : Service Provider Interfaces


Java提供了一种方式, 让我们可以对接口的实现进行动态替换, 这就是SPI机制. SPI可以降低依赖。尤其在Android项目模块化中极为有用。
比如美团和猫眼的项目中都用到了ServiceLoader。可以实现模块之间不会基于具体实现类硬编码，可插拔。

ServiceLoader是实现SPI一个重要的类。是jdk6里面引进的一个特性。在资源目录META-INF/services中放置提供者配置文件，然后在app运行时，
遇到Serviceloader.load(XxxInterface.class)时，会到META-INF/services的配置文件中寻找这个接口对应的实现类全路径名，
然后使用反射去生成一个无参的实例。

ServiceLoader还有一个特定的限制，就是我们提供的这些具体实现的类必须提供无参数的构造函数，否则ServiceLoader就会报错。


WMRouter源码学习

我们以一个最简单的使用场景来分析。
1. 初始化
```
class App : Application() {


    override fun onCreate() {
        super.onCreate()
        val rootUriHandler = DefaultRootUriHandler(this)
        Router.init(rootUriHandler)
    }
}

```
2.使用注解，注册跳转路径
```
@RouterUri(
    path = ["/TestBasicActivity"]
)
class TestBasicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Basic activity"
        setContentView(R.layout.activity_test_basic)
    }
}

```
3.跳转到指定界面
```
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnJump.setOnClickListener {
            Router.startUri(this, "/TestBasicActivity")
        }

}


```

在使用注解注册跳转路径以后，build一下工程，可以在`WMRouterDemo/app/build/generated/source/kapt/debug/com/sankuai/waimai/router/generated/`
目录下找到注解自动生成的文件。

UriAnnotationInit_7faf56b0e560a0b80e7feccbda06ab16.java

```
package com.sankuai.waimai.router.generated;

import com.dmw.wmrouterdemo.advanced.abtest.HomeABTestHandler;
import com.dmw.wmrouterdemo.advanced.account.LoginInterceptor;
import com.sankuai.waimai.router.common.IUriAnnotationInit;
import com.sankuai.waimai.router.common.UriAnnotationHandler;

public class UriAnnotationInit_7faf56b0e560a0b80e7feccbda06ab16 implements IUriAnnotationInit {
  public void init(UriAnnotationHandler handler) {
  	  //调用UriAnnotationHandler的register方法
    handler.register("", "", "/TestBasicActivity", "com.dmw.wmrouterdemo.activity.TestBasicActivity", false);
  }
}


```
UriAnnotationHandler的register方法
```
 public void register(String scheme, String host, String path,
                         Object handler, boolean exported, UriInterceptor... interceptors) {
        // 没配scheme和host使用默认值
        if (TextUtils.isEmpty(scheme)) {
            scheme = mDefaultScheme;
        }
        if (TextUtils.isEmpty(host)) {
            host = mDefaultHost;
        }
        //拼接成scheme://host
        String schemeHost = RouterUtils.schemeHost(scheme, host);
       	//获取路径处理者，如果为null，则创建并保存在map中
        PathHandler pathHandler = mMap.get(schemeHost);
        if (pathHandler == null) {
        	  //注释1处，创建处理者
            pathHandler = createPathHandler();
            //加入到map中
            mMap.put(schemeHost, pathHandler);
        }
        //注释2处
        pathHandler.register(path, handler, exported, interceptors);
    }
    
    
```

在注释1处，UriAnnotationHandler的createPathHandler方法
```
@NonNull
    protected PathHandler createPathHandler() {
        PathHandler pathHandler = new PathHandler();
        if (sAddNotFoundHandler) {
            pathHandler.setDefaultChildHandler(NotFoundHandler.INSTANCE);
        }
        return pathHandler;
    }
    
    
```
注意一下`sAddNotFoundHandler`这个变量，默认为true。如果`sAddNotFoundHandler`为`true`，则每个`PathHandler`上添加一个`NotFoundHandler`。
如果添加了`NotFoundHandler`，则只要`scheme+host`匹配上，所有的URI都会被`PathHandler`拦截掉，即使path不能匹配，也会分发到`NotFoundHandler`终止分发。
createPathHandler方法最终构建了一个PathHandler对象，并且该对象添加了一个默认的子处理者，一个`NotFoundHandler`对象。
然后把这个PathHandler对象加入到了UriAnnotationHandler的map中。

```
/**
     * key是由scheme+host生成的字符串，value是PathHandler。
     */
    private final Map<String, PathHandler> mMap = new HashMap<>();
```

在上面方法的注释2处，调用了PathHandler的register方法。
```
/**
     * 注册一个子节点
     *
     * @param path         path
     * @param target       支持ActivityClassName、ActivityClass、UriHandler
     * @param exported     是否允许外部跳转
     * @param interceptors 要添加的interceptor
     */
    public void register(String path, Object target, boolean exported,
            UriInterceptor... interceptors) {
        if (!TextUtils.isEmpty(path)) {
        	  //如果path不是以反斜杠开头，则添加反斜杠
            path = RouterUtils.appendSlash(path);
            //注释1处，
            UriHandler parse = UriTargetTools.parse(target, exported, interceptors);
            UriHandler prev = mMap.put(path, parse);
            if (prev != null) {
                Debugger.fatal("[%s] 重复注册path='%s'的UriHandler: %s, %s", this, path, prev, parse);
            }
        }
    }
    
    
```

在PathHandler的register方法的注释1处，调用了UriTargetTools的parse方法
```
public static UriHandler parse(Object target, boolean exported,
            UriInterceptor... interceptors) {
        //注释1处
        UriHandler handler = toHandler(target);
        if (handler != null) {
            if (!exported) {//在这个例子中，我们传入的exported是false，不允许外部跳转。添加NotExportedInterceptor
                handler.addInterceptor(NotExportedInterceptor.INSTANCE);
            }
            handler.addInterceptors(interceptors);
        }
        return handler;
    }


```
在UriTargetTools的parse方法的注释1处，调用了toHandler方法
```
private static UriHandler toHandler(Object target) {
        if (target instanceof UriHandler) {
            return (UriHandler) target;
        } else if (target instanceof String) {
            return new ActivityClassNameHandler((String) target);
        } else if (target instanceof Class && isValidActivityClass((Class) target)) {
            //noinspection unchecked
            return new ActivityHandler((Class<? extends Activity>) target);
        } else {
            return null;
        }
    }
    
```
在这个例子中我们的target是String类型，所以会返回一个ActivityClassNameHandler对象。
然后给这个ActivityClassNameHandler对象添加了一个NotExportedInterceptor拦截器，不允许外部跳转。

最后我们把这个ActivityClassNameHandler对象，加入到了PathHandler的mMap对象中。

```
private final CaseInsensitiveNonNullMap<UriHandler> mMap = new CaseInsensitiveNonNullMap<>();

```

到这里第2步基本完毕，现在看一下第1步，初始化过程。

DefaultRootUriHandler部分代码


```
 public class DefaultRootUriHandler extends RootUriHandler {

    private final PageAnnotationHandler mPageAnnotationHandler;
    private final UriAnnotationHandler mUriAnnotationHandler;
    private final RegexAnnotationHandler mRegexAnnotationHandler;

    public DefaultRootUriHandler(Context context) {
        this(context, null, null);
    }

    /**
     * @param defaultScheme {@link RouterUri} 没有指定scheme时，则使用这里设置的defaultScheme
     * @param defaultHost   {@link RouterUri} 没有指定host时，则使用这里设置的defaultHost
     */
    public DefaultRootUriHandler(Context context,
                                 @Nullable String defaultScheme, @Nullable String defaultHost) {
        super(context);
        mPageAnnotationHandler = createPageAnnotationHandler();
        //注意这里创建的是一个UriAnnotationHandler对象
        mUriAnnotationHandler = createUriAnnotationHandler(defaultScheme, defaultHost);
        mRegexAnnotationHandler = createRegexAnnotationHandler();

        // 按优先级排序，数字越大越先执行

        // 处理RouterPage注解定义的内部页面跳转，如果注解没定义，直接结束分发
        addChildHandler(mPageAnnotationHandler, 300);
        // 处理RouterUri注解定义的URI跳转，如果注解没定义，继续分发到后面的Handler
        addChildHandler(mUriAnnotationHandler, 200);
        // 处理RouterRegex注解定义的正则匹配
        addChildHandler(mRegexAnnotationHandler, 100);
        // 添加其他用户自定义Handler...

        // 都没有处理，则尝试使用默认的StartUriHandler直接启动Uri
        addChildHandler(new StartUriHandler(), -100);
        // 全局OnCompleteListener，用于输出跳转失败提示信息
        setGlobalOnCompleteListener(DefaultOnCompleteListener.INSTANCE);
    }
    //...

}
```
我们看一下addChildHandler方法，这个方法是在ChainedHandler中定义的。

```
private final PriorityList<UriHandler> mHandlers = new PriorityList<>();

    /**
     * 添加一个Handler
     *
     * @param priority 优先级。优先级越大越先执行；相同优先级，先加入的先执行。
     */
    public ChainedHandler addChildHandler(@NonNull UriHandler handler, int priority) {
        mHandlers.addItem(handler, priority);
        return this;
    }

```
这里面就是一个按照传入的优先级进行排序的List。

Router的init方法

```
Router.init(rootUriHandler)

```

就是给ROOT_HANDLER赋值。

```
public static void init(@NonNull RootUriHandler rootUriHandler) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Debugger.fatal("初始化方法init应该在主线程调用");
        }
        if (ROOT_HANDLER == null) {
            ROOT_HANDLER = rootUriHandler;
        } else {
            Debugger.fatal("请勿重复初始化UriRouter");
        }
    }
```

然后看第3步
```
Router.startUri(this, "/TestBasicActivity")

```

```
public static void startUri(Context context, String uri) {
	  //构建一个UriRequest，然后调用DefaultRootUriHandler的startUri方法
    getRootHandler().startUri(new UriRequest(context, uri));
}

```

getRootHandler()获取的是我们传入的的DefaultRootUriHandler对象。DefaultRootUriHandler继承了RootUriHandler，startUri方法是在RootUriHandler类中声明的。

RootUriHandler的startUri方法
```
public void startUri(@NonNull UriRequest request) {
        if (request == null) {//注释1处，

            String msg = "UriRequest为空";
            Debugger.fatal(msg);
            UriRequest req = new UriRequest(mContext, Uri.EMPTY).setErrorMessage(msg);
            onError(req, UriResult.CODE_BAD_REQUEST);

        } else if (request.getContext() == null) {//注释2处

            String msg = "UriRequest.Context为空";
            Debugger.fatal(msg);
            UriRequest req = new UriRequest(mContext, request.getUri(), request.getFields())
                    .setErrorMessage(msg);
            onError(req, UriResult.CODE_BAD_REQUEST);

        } else if (request.isUriEmpty()) {//注释3处

            String msg = "跳转链接为空";
            Debugger.e(msg);
            request.setErrorMessage(msg);
            onError(request, UriResult.CODE_BAD_REQUEST);

        } else {

            if (Debugger.isEnableLog()) {
                Debugger.i("");
                Debugger.i("---> receive request: %s", request.toFullString());
            }
            //注释4处
            handle(request, new RootUriCallback(request));
        }
    }
```

注释1，2，3处都是为了处理异常情况，我们先看看onError方法。
```
private void onError(@NonNull UriRequest request, int resultCode) {
        OnCompleteListener globalListener = mGlobalOnCompleteListener;
        if (globalListener != null) {
            globalListener.onError(request, resultCode);
        }
        final OnCompleteListener listener = request.getOnCompleteListener();
        if (listener != null) {
            listener.onError(request, resultCode);
        }
    }
    
    
```
在这个方法里面，如果设置了全局的OnCompleteListener，首先是调用全局的OnCompleteListener的onError方法，
如果request设置了OnCompleteListener，则调用request的OnCompleteListener。我们在初始化Router的时候设置了的全局OnCompleteListener是一个DefaultOnCompleteListener对象。

DefaultOnCompleteListener的onError方法，只是给出了一个toast提示。
```
 @Override
    public void onError(@NonNull UriRequest request, int resultCode) {
        String text = request.getStringField(UriRequest.FIELD_ERROR_MSG, null);
        if (TextUtils.isEmpty(text)) {
            switch (resultCode) {
                case CODE_NOT_FOUND:
                    text = "不支持的跳转链接";
                    break;
                case CODE_FORBIDDEN:
                    text = "没有权限";
                    break;
                default:
                    text = "跳转失败";
                    break;
            }
        }
        text += "(" + resultCode + ")";
        if (Debugger.isEnableDebug()) {
            text += "\n" + request.getUri().toString();
        }
        Toast.makeText(request.getContext(), text, Toast.LENGTH_LONG).show();
    }
```
我们接下来看RootUriHandler的startUri方法的注释4处,调用handle方法。
```
 handle(request, new RootUriCallback(request));


```

首先使用传入的request构建了一个RootUriCallback对象。
```
protected class RootUriCallback implements UriCallback {

        private final UriRequest mRequest;

        public RootUriCallback(UriRequest request) {
            mRequest = request;
        }

        @Override
        public void onNext() {
            onComplete(CODE_NOT_FOUND);
        }

        @Override
        public void onComplete(int resultCode) {
            switch (resultCode) {

                case CODE_REDIRECT:
                    // 重定向，重新跳转
                    Debugger.i("<--- redirect, result code = %s", resultCode);
                    startUri(mRequest);
                    break;

                case CODE_SUCCESS:
                    // 跳转成功
                    mRequest.putField(UriRequest.FIELD_RESULT_CODE, resultCode);
                    onSuccess(mRequest);
                    Debugger.i("<--- success, result code = %s", resultCode);
                    break;

                default:
                    // 跳转失败
                    mRequest.putField(UriRequest.FIELD_RESULT_CODE, resultCode);
                    onError(mRequest, resultCode);
                    Debugger.i("<--- error, result code = %s", resultCode);
                    break;
            }
        }
    }
    
```




[ServiceLoader使用及原理分析](https://blog.csdn.net/a910626/article/details/78811273)

[ServiceLoader跟DriverManager使用总结](https://blog.csdn.net/liangyihuai/article/details/50716035)