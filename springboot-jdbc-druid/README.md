我们都使用过连接池，比如C3P0，DBCP，hikari， Druid，虽然HikariCP的速度稍快，但Druid能够提供强大的监控和扩展功能，也是阿里巴巴的开源项目。
Druid是阿里巴巴开发的号称为监控而生的数据库连接池，在功能、性能、扩展性方面，都超过其他数据库连接池，包括DBCP、C3P0、BoneCP、Proxool、JBoss DataSource等等，秒杀一切。
Druid可以很好的监控DB池连接和SQL的执行情况，天生就是针对监控而生的DB连接池。
Spring Boot默认数据源HikariDataSource与JdbcTemplate中已经介绍		Spring Boot 2.x默认使用Hikari数据源，可以说Hikari与Driud都是当前Java Web上最优秀的数据源。
而Druid已经在阿里巴巴部署了超过600个应用，经过好几年生产环境大规模部署的严苛考验！

- **stat：** Druid内置提供一个StatFilter，用于统计监控信息。
- **wall：** Druid防御SQL注入攻击的WallFilter就是通过Druid的SQL Parser分析。Druid提供的SQL Parser可以在JDBC层拦截SQL做相应处理，比如说分库分表、审计等。
- **log4j2：** 这个就是 日志记录的功能，可以把sql语句打印到log4j2供排查问题。
## 1. 相关配置
### 1.1 添加依赖

```
<properties>
    <java.version>1.8</java.version>
    <alibabaDruidStarter.version>1.2.11</alibabaDruidStarter.version>
</properties>

<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>${alibabaDruidStarter.version}</version>
</dependency>
```
### 1.2 配置属性

- **配置Druid数据源（连接池）：** 如同c3p0、dbcp数据源可以设置数据源连接初始化大小、最大连接数、等待时间、最小连接数 等一样，Druid数据源同理可以进行设置。
- **配置Druid web监控filter（WebStatFilter）：** 这个过滤器的作用就是统计web应用请求中所有的数据库信息，比如 发出的sql语句，sql执行的时间、请求次数、请求的url地址、以及seesion监控、数据库表的访问次数等等。
- **配置Druid后台管理Servlet（StatViewServlet）：** Druid数据源具有监控的功能，并提供了一个web界面方便用户查看，类似安装 路由器 时，人家也提供了一个默认的web页面；需要设置Druid的后台管理页面的属性，比如 登录账号、密码等。

【注意】：Druid Spring Boot Starter配置属性的名称完全遵照Druid，可以通过Spring Boot配置文件来配置Druid数据库连接池和监控，如果没有配置则使用默认值，如下在application.yml配置相关属性：
上述配置文件的参数可以在com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties和 org.springframework.boot.autoconfigure.jdbc.DataSourcePropertie中找到。
### 1.3 配置Filter
可以通过spring.datasource.druid.filters=stat,wall,log4j ...的方式来启用相应的内置Filter，不过这些Filter都是默认配置。如果默认配置不能满足需求，可以放弃这种方式，通过配置文件来配置Filter，如下所示：
目前为以下Filter提供了配置支持，根据（spring.datasource.druid.filter.*）进行配置。

- StatFilter
- WallFilter
- ConfigFilter
- EncodingConvertFilter
- Slf4jLogFilter
- Log4jFilter
- Log4j2Filter
- CommonsLogFilter

不想使用内置的Filters，要想使自定义Filter配置生效需要将对应Filter的enabled设置为true，Druid Spring Boot Starter默认禁用StatFilter，可以将其enabled设置为true来启用它。
## 2.监控页面
2.1 启动项目后，访问http://localhost:8081/druid/login.html来到登录页面，输入用户名密码登录，如下所示：
![](https://cdn.nlark.com/yuque/0/2023/png/12547636/1680686497780-9e5bbf30-66b8-42a6-8121-8f302585b541.png#averageHue=%23f1f1f1&clientId=uaf18a025-a18a-4&from=paste&id=ub23bde57&originHeight=539&originWidth=1080&originalType=url&ratio=2&rotation=0&showTitle=false&status=done&style=none&taskId=uf641b930-c515-4412-ae4a-e00bbaf0d0d&title=)
2.2 数据源页面是当前DataSource配置的基本信息，上述配置的Filter可以在里面找到，如果没有配置 Filter（一些信息会无法统计，例如SQL监控会无法获取JDBC相关的SQL执行信息）
![](https://cdn.nlark.com/yuque/0/2023/png/12547636/1680686497783-cb221a67-19c8-4f2e-8165-b69d81e289f0.png#averageHue=%23f9f7f6&clientId=uaf18a025-a18a-4&from=paste&id=ue727709e&originHeight=478&originWidth=1080&originalType=url&ratio=2&rotation=0&showTitle=false&status=done&style=none&taskId=u7fa6d21d-2d08-4020-aab4-96752833396&title=)
2.3 SQL监控页面，统计了所有SQL语句的执行情况
![](https://cdn.nlark.com/yuque/0/2023/png/12547636/1680686497780-9375c44d-37c6-42dc-9344-192982dcbf90.png#averageHue=%23f1f1f0&clientId=uaf18a025-a18a-4&from=paste&id=u0cadeec9&originHeight=101&originWidth=1080&originalType=url&ratio=2&rotation=0&showTitle=false&status=done&style=none&taskId=ubf92268d-70b2-4d19-80d9-02a7887bb90&title=)
2.4 URL监控页面，统计了所有Controller接口的访问以及执行情况
![](https://cdn.nlark.com/yuque/0/2023/png/12547636/1680686497794-a1b5a3f7-d24c-4f64-bd17-259358b45c01.png#averageHue=%23f1f1f1&clientId=uaf18a025-a18a-4&from=paste&id=ucf6df3cb&originHeight=110&originWidth=1080&originalType=url&ratio=2&rotation=0&showTitle=false&status=done&style=none&taskId=uf7fb1bd9-ed7e-424f-b9a3-3102fd30532&title=)
2.5 Spring监控页面，利用aop对指定接口的执行时间，jdbc数进行记录
![](https://cdn.nlark.com/yuque/0/2023/png/12547636/1680686498215-553d10cc-5327-4f75-a90d-93415efdfe2c.png#averageHue=%23f5f4f4&clientId=uaf18a025-a18a-4&from=paste&id=u9121de63&originHeight=135&originWidth=1080&originalType=url&ratio=2&rotation=0&showTitle=false&status=done&style=none&taskId=u15eab718-871c-4984-9e85-f3dc22024e6&title=)
2.6 SQL防火墙页面
druid提供了黑白名单的访问，可以清楚的看到sql防护情况。
2.7 Session监控页面
可以看到当前的session状况，创建时间、最后活跃时间、请求次数、请求时间等详细参数。
2.8 JSONAPI页面
通过api的形式访问Druid的监控接口，api接口返回Json形式数据。
## 3. sql监控
配置Druid web监控filter（WebStatFilter）这个过滤器，作用就是统计web应用请求中所有的数据库信息，比如 发出的sql语句，sql执行的时间、请求次数、请求的url地址、以及seesion监控、数据库表的访问次数，如下配置：
## 4. 慢sql记录
有时候，系统中有些SQL执行很慢，我们希望使用日志记录下来，可以开启Druid的慢SQL记录功能，如下配置：
启动后，如果遇到执行慢的SQL，便会输出到日志中
## 5. spring 监控
访问之后spring监控默认是没有数据的，但需要导入SprngBoot的AOP的Starter，如下所示：
同时需要在application.yml按如下配置:
Spring监控AOP切入点，如com.springboot.template.dao.*,配置多个英文逗号分隔
## 6. 去广告(Ad)
访问监控页面的时候，你可能会在页面底部（footer）看到阿里巴巴的广告，如下所示：
![](https://cdn.nlark.com/yuque/0/2023/png/12547636/1680686498298-2dfb85fc-1df5-4331-a5eb-80e51be31522.png#averageHue=%23535758&clientId=uaf18a025-a18a-4&from=paste&id=u6715440e&originHeight=145&originWidth=1080&originalType=url&ratio=2&rotation=0&showTitle=false&status=done&style=none&taskId=ud80a0213-b090-4681-ba1b-193a6587581&title=)
原因：引入的druid的jar包中的common.js(里面有一段js代码是给页面的footer追加广告的)
如果想去掉，有两种方式：
### 6.1 直接手动注释这段代码
如果是使用Maven，直接到本地仓库中，查找这个jar包，注释如下代码：
common.js的位置：
### 6.2 使用过滤器过滤
注册一个过滤器，过滤common.js的请求，使用正则表达式替换相关的广告内容，如下代码所示：
两种方式都可以，建议使用的是第一种，从根源解决。
## 7. 获取 Druid 的监控数据
Druid的监控数据可以在开启StatFilter后，通过DruidStatManagerFacade进行获取;
DruidStatManagerFacade#getDataSourceStatDataList该方法可以获取所有数据源的监控数据，除此之外DruidStatManagerFacade还提供了一些其他方法，可以按需选择使用。

