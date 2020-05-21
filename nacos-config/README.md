## 1. Nacos简介
Nacos 致力于帮助您发现、配置和管理微服务。Nacos 提供了一组简单易用的特性集，帮助您快速实现动态服务发现、服务配置、服务元数据及流量管理。

Nacos 帮助您更敏捷和容易地构建、交付和管理微服务平台。 Nacos 是构建以“服务”为中心的现代应用架构 (例如微服务范式、云原生范式) 的服务基础设施。
参考：[https://nacos.io/zh-cn/docs/what-is-nacos.html](https://nacos.io/zh-cn/docs/what-is-nacos.html)

## 2. 安装部署Nacos
单机模式 - 用于测试和单机试用。
集群模式 - 用于生产环境，确保高可用。
多集群模式 - 用于多数据中心场景。
### 2.1 单机运行
#### Linux/Unix/Mac
*   Standalone means it is non-cluster Mode. * sh [startup.sh](http://startup.sh/) -m standalone
#### Windows
*  cmd startup.cmd 或者双击 startup.cmd 文件

参考：[https://nacos.io/zh-cn/docs/deployment.html](https://nacos.io/zh-cn/docs/deployment.html)


## 3. 添加Nacos命名空间
在Nacos后台新增一个名为`loamen-demo`的命名空间
![image.png](https://upload-images.jianshu.io/upload_images/5747348-1084130b453737cc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
创建完成后可以看到新命名空间的ID为：`af24bcf1-6f21-498d-90bf-465ed1d02ac6`
![image.png](https://upload-images.jianshu.io/upload_images/5747348-74ac6ae1b2ccb0ef.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 4. 添加Nacos配置
回到配置列表，新增一个配置，注意命名空间选择刚才创建的`loamen-demo`，如图：
![image.png](https://upload-images.jianshu.io/upload_images/5747348-ce634c3e160ba7d3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
添加一个Nacos配置，`Data ID`为`loamen-config-demo.yaml`，类型选`YAML`如图：
![image.png](https://upload-images.jianshu.io/upload_images/5747348-983628761708f52e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
内容如下：
```
server:
    name: test
```
## 5.Spring Boot工程
在`pom`中加入Nacos依赖项
```
<dependency>
            <groupId>com.alibaba.boot</groupId>
            <artifactId>nacos-config-spring-boot-starter</artifactId>
            <version>${nacos.version}</version>
</dependency>
```
在`application.yml`加入配置项，注意这里的`namespace`为刚才Nacos配置中的`命名空间ID`而不是`命名空间名称`
```
spring:
  application:
    name: nacos-config

nacos:
  config:
    server-addr: 127.0.0.1:8848
    namespace: af24bcf1-6f21-498d-90bf-465ed1d02ac6 # 命名空间ID
    data-ids: loamen-config-demo.yaml

server:
  port: 8800
```
在`Application`加入`NacosConfigurationProperties`注解，`dataId`为刚才创建的`loamen-config-demo.yaml`
```
@SpringBootApplication
@NacosConfigurationProperties(dataId = "loamen-config-demo.yaml", autoRefreshed = true)
public class NacosConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosConfigApplication.class, args);
    }
}
```
新建一个`DemoController`，代码如下：
```
@RestController
@RequestMapping("config")
public class DemoController {
    @NacosValue(value = "${server.name:1}", autoRefreshed = true) //取得Nacos配置中的`server.name`配置项
    private String serverName;

    @RequestMapping(value = "/get", method = GET)
    @ResponseBody
    public String get() {
        return serverName;
    }
}
```
运行访问`http://localhost:8800/config/get`可以看到结果
![Nacos配置读取](https://upload-images.jianshu.io/upload_images/5747348-b71cccd8543ebf1e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
在Nacos中将值修改为`loamen.com`，刷新刚才页面看看效果
![修改Nacos配置](https://upload-images.jianshu.io/upload_images/5747348-aa5b3982a9e18474.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![loamen.com](https://upload-images.jianshu.io/upload_images/5747348-8b3e58b69b7459ff.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 6. 示例代码
[https://github.com/loamen/spring-cloud-demo/tree/master/nacos-config](https://github.com/loamen/spring-cloud-demo/tree/master/nacos-config)


## 7. 文章链接
[https://xie.infoq.cn/article/34e5cbc9be69f9968092c994f](https://xie.infoq.cn/article/34e5cbc9be69f9968092c994f)