1. 主要技术栈
    1. 前端
        1. Thymeleaf
        2. Bootstrap
        3. JQuery
    2. 后端
        1. SpringBoot
        2. JSR303
        3. MyBatis
    3. 中间件
        1. RabbitMQ
        2. Redis
        3. Druid
2. 登录功能
    1. 两次MD5
        1. 用户端：PASS = MD5(明文+固定Salt)
        2. 服务端：PASS = MD5(用户输入+随机Salt)
    2. JSR303校验
        1. 自定义校验器，实现通过注解完成校验，减少重复代码
    3. 全局通用异常处理
        1. 自定义全局异常，出现不符合要求的情况，统一抛出该异常，避免用返回值来处理
        2. 自定义全局异常处理器，根据异常类型，返回对应的Result
    4. 分布式Session
        1. 将cookie中保存的token作为key，用户信息作为value，保存到redis中，这样通过redis就可以查询到是某个用户发起的请求
        2. 通过集成WebMvcConfigurerAdapter，编写配置类，重写addArgumentResolvers方法，实现HandlerMethodArgumentResolver接口，完成自定义参数解析器，使得User可以作为变量，在方法调用时直接封装，减少重复代码。
3. 页面优化技术
    1. 页面缓存+对象缓存
        1. 页面缓存
            1. 取缓存，查询缓存中是否有当前页面的缓存
            2. 手动渲染模板，未查询到缓存后，查询数据库并手动渲染模板，设定过期时间并将模板保存到缓存中
            3. 结果输出
        2. 对象缓存
            1. 将热点数据保存到redis中
            2. 对对象处理后，并完成数据库更新后，再删除或更新redis中的数据。
    2. 页面静态化，前后端分离 
        1. 通过前后端分离，减少数据交互，提高QPS。
4. 防止超卖
    1. 数据库更新时，利用数据库的锁，当且仅当库存大于等于1时才更新数据库，避免出现两个用户同时抢到最后一个商品。
    2. 利用唯一索引，关联用户ID和商品ID，避免用户同时发起两个请求，导致同一个用户抢到两个。
5. 接口优化
    1. Redis预减库存减少数据库访问
        1. 预加载秒杀商品到Redis
        2. 收到请求后，Redis预减库存，这样可以减少大量的数据库查询。
        3. 请求入队，返回排队中，异步下单
        4. 请求出兑，生成订单，减少库存
        5. 客户端轮训，是否秒杀成功。
    2. 内存标记减少Redis访问
        1. 本地保存一个标志位，标记秒杀是否结束，减少Redis的访问
6. 秒杀接口地址隐藏
    1. 将接口带上PathVariable参数
    2. 添加生成地址的接口
    3. 收到请求后，先验证PathVariable
7. 数学公式验证码
    1. 输入验证码，分散用户请求
    2. 防止机器人