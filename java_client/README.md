环境为jdk17
执行之后会在当前项目所处磁盘根路径生成一个exec文件夹,然后会把src/main/resources/exec下的文件放在那避免因为路径问题出错

项目启动之后,会生成一个tcp服务端,用来接受hook信息,然后把接收的信息放在队列中,之后用一个线程去循环处理消息.
具体实现可以看
```com.example.wxhk.tcp.vertx```包下的三个文件

com.example.wxhk.tcp.vertx.VertxTcp  这个是tcp服务端,接受信息

com.example.wxhk.tcp.vertx.InitWeChat 微信环境初始化

com.example.wxhk.tcp.vertx.ArrHandle 循环消息处理


启动项目需要去修改配置文件的微信路径
