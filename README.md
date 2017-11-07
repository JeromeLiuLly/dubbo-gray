# dubbo-gray
基于Dubbo实现的灰度发布

## 架构设计和界面

### 架构模型

### 平台化操作
![平台化界面图](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/平台化操作.png)

## 项目结构
### dubbo-gray-common 基础操作工具包
项目类型：jar

### dubbo-gray-xxx 服务消费者
调用服务提供者和服务提供者，验证是否进入灰度服务。

### dubbo-gray-core 框架核心包
核心jar包，所有微服务均引用该包，用于负载自定义策略规则，是实现灰度发布的核心架包。

### dubbo-gray-xxx-api api接口包
项目类型：jar

### dubbo-gray-entry 网关
拉取灰度策略，进行请求的把标签操作。

## 应用场景

### 灰度发布
通过灰度版本的控制，实现符合灰度策略的对象，优先进入灰度服务进行体验。

### 异构服务的共存
例如，根据不同的策略，有根据不同的渠道、地域、门店、品牌等，优先使用不同的服务。例如，广州地域的用户，仅能使用基于广州部署的微服务。

### 同等级服务的调用
例如，业务场景，根据不同的渠道和来源进行下单。微信的下单，仅能调用微信的order-service服务;官网下单，仅能调用官网的order-service下单;
通过这样的方式，上层业务无须调用何种具体服务统一底层进行负载调用，实现业务的解耦和服务的可插拔配置；

## 测试
测试/验证流程说明：

### 第一步 === 测试 灰度开关是关闭状态[任何角色]
走正常标签【release】,并进行轮询策略

预先设置好release标签
![图一](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第一步-release-1.png)

3.执行结果
  如下显示
![图二](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第一步-release-result.png)

### 第二步 === 测试 灰度开关是开启状态[正常角色][灰度角色]
说明下，灰度服务和正常服务不能并存在同一个服务里面。
例如:dubbo-gray-user-provider 10.200.102.95:20898 既是灰度服务，又是正常服务。

![图三](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第二步-正常角色-release.png)

正常角色:走正常标签【release】,并进行轮询策略
![图四](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第二步-正常角色-release-result.png)

灰度角色:走灰度标签【灰度配置信息】,根据灰度策略进行路由访问
![图五](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第二步-灰度角色-gray.png)

### 第三步 === 测试 灰度开关是开启状态,未能找到灰度服务,执行默认正常策略[灰度角色]
![图六](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第三步-灰度角色-release.png)

灰度角色:走灰度标签【灰度配置信息】,未能发现灰度服务,走正常服务。
![图七](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第三步-灰度角色-release-result.png)

### 第四步 === 测试 正常业务化的操作【以下服务，均是开启了灰度开关】

#### 方式1------(灰度用户): 前端 ==> 网关 ==> 正常服务 ==> 灰度服务

准备工作：
1.启动所有服务：
【order-service服务，order-service2服务，user-service服务，user-service2服务】

2.设置灰度标签和正常服务标签
 设置order-service服务为【正常标签】，user-service服务为【灰度标签】，其他服务均不设置。
![图八](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第四步-灰度角色-1.png)

![图九](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第四步-灰度角色-2.png)

![图十](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第四步-灰度角色-3.png)

3.执行结果
  如下显示
![图十一](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第四步-灰度角色-result.png)

4.随机组合处理
4.1.1 配置多个order-service服务为正常服务，测试是否进行轮询操作：
![图十一](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第四步-随机策略-1-1.png)

![图十一](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第四步-随机策略-1-2.png)

![图十一](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第四步-随机策略-1-3.png)

4.1.2	配置多个user-service服务为灰度服务，测试是否进行轮询操作：

配置信息
![图十一](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第四步-随机策略-2-1.png)

![图十一](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第四步-随机策略-2-2.png)

![图十一](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第四步-随机策略-2-3.png)


#### 方式2------(灰度用户): 前端 ==> 网关 ==> 灰度服务 ==> 正常服务

准备工作：
1.	复原灰度标签和正常标签操作，根据标签和服务信息，触发对应的灰度服务列表【分离】

2.	设置灰度标签和正常服务标签
 设置user-service服务为【灰度标签】，order-service服务为【正常标签】，其他服务均不设置。
![图十一](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第五步-灰度角色-1.png)

![图十一](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第五步-灰度角色-2.png)

3.执行结果
  如下显示
![图十一](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第五步-灰度角色-3.png)

![图十一](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第五步-灰度角色-4.png)

![图十一](https://raw.githubusercontent.com/JeromeLiuLly/dubbo-gray/master/img/第五步-灰度角色-5.png)