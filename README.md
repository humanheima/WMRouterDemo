SPI : Service Provider Interfaces


Java提供了一种方式, 让我们可以对接口的实现进行动态替换, 这就是SPI机制. SPI可以降低依赖。尤其在Android项目模块化中极为有用。
比如美团和猫眼的项目中都用到了ServiceLoader。可以实现模块之间不会基于具体实现类硬编码，可插拔。

ServiceLoader是实现SPI一个重要的类。是jdk6里面引进的一个特性。在资源目录META-INF/services中放置提供者配置文件，然后在app运行时，
遇到Serviceloader.load(XxxInterface.class)时，会到META-INF/services的配置文件中寻找这个接口对应的实现类全路径名，
然后使用反射去生成一个无参的实例。

ServiceLoader还有一个特定的限制，就是我们提供的这些具体实现的类必须提供无参数的构造函数，否则ServiceLoader就会报错。


[ServiceLoader使用及原理分析](https://blog.csdn.net/a910626/article/details/78811273)

[ServiceLoader跟DriverManager使用总结](https://blog.csdn.net/liangyihuai/article/details/50716035)