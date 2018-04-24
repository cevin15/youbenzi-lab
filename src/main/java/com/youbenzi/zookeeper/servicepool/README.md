# 基于 Zookeeper 的服务池(入门测试代码例子)

使用Zookeeper来实现的服务池，保障业务的高可用。

## 用法
1. 初始化连接池：`ServicePool.ME.init(String connectString, int timout)`
1. 服务端注册服务：`ServicePool.ME.register(String service, String host)` 
1. 客户端获取服务地址：`ServicePool.ME.getHost(String service)`
1. 关闭连接池：`ServicePool.ME.close()`