# Common模块使用示例

## 1. 引入依赖
在需要使用的模块pom.xml中添加：
```xml
<dependency>
    <groupId>com.cobazaar</groupId>
    <artifactId>common</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 2. 统一响应结果使用
```java
@RestController
public class UserController {
    
    @GetMapping("/user/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            return Result.success(user);
        } catch (Exception e) {
            return Result.fail("用户不存在");
        }
    }
    
    @PostMapping("/user")
    public Result<String> createUser(@RequestBody User user) {
        // 参数校验
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return Result.paramError();
        }
        
        userService.save(user);
        return Result.success("用户创建成功", "user_" + user.getId());
    }
    
    @DeleteMapping("/user/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        // 权限校验
        if (!currentUserHasPermission()) {
            return Result.unauthorized();
        }
        
        userService.deleteById(id);
        return Result.success();
    }
}
```

## 3. Redis工具类使用
```java
@Service
public class UserService {
    
    @Autowired
    private RedisUtils redisUtils;
    
    public User findById(Long id) {
        // 先从缓存获取
        String cacheKey = Constants.Redis.PREFIX_USER + id;
        User user = (User) redisUtils.get(cacheKey);
        
        if (user == null) {
            // 缓存不存在，查询数据库
            user = userMapper.selectById(id);
            if (user != null) {
                // 存入缓存，设置1小时过期
                redisUtils.set(cacheKey, user, Constants.Redis.DEFAULT_EXPIRE);
            }
        }
        
        return user;
    }
    
    public void updateUser(User user) {
        userMapper.updateById(user);
        // 更新后删除缓存
        String cacheKey = Constants.Redis.PREFIX_USER + user.getId();
        redisUtils.del(cacheKey);
    }
}
```

## 4. 异常处理使用
```java
@Service
public class OrderService {
    
    public Order createOrder(OrderDTO orderDTO) {
        // 参数校验
        if (orderDTO.getUserId() == null) {
            throw new BizException(ResponseEnum.PARAM_ERROR, "用户ID不能为空");
        }
        
        if (orderDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("订单金额必须大于0");
        }
        
        // 业务逻辑处理
        Order order = new Order();
        // ... 设置订单属性
        orderMapper.insert(order);
        
        return order;
    }
}
```

## 5. 常量使用
```java
@RestController
public class AuthController {
    
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDTO loginDTO) {
        // 验证请求头
        String authorization = request.getHeader(Constants.Http.HEADER_AUTHORIZATION);
        
        // 业务逻辑
        String token = authService.login(loginDTO);
        
        // 设置响应头
        response.setHeader(Constants.Http.HEADER_GATEWAY_VERSION, "1.0.0");
        
        return Result.success("登录成功", token);
    }
}
```

## 6. Request包装使用
```java
@Component
public class LogFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        // 由于使用了RequestBodyWrapperFilter，这里可以多次读取request body
        String requestBody = HttpHelper.getBodyString(request);
        String ipAddress = HttpHelper.getIpAddress((HttpServletRequest) request);
        
        // 记录日志
        log.info("请求IP: {}, 请求体: {}", ipAddress, requestBody);
        
        chain.doFilter(request, response);
    }
}
```

## 7. Spring上下文工具使用
```java
@Component
public class SomeUtilClass {
    
    public void someMethod() {
        // 在非Spring管理的类中获取Bean
        RedisUtils redisUtils = SpringContextHolder.getBean(RedisUtils.class);
        UserService userService = SpringContextHolder.getBean(UserService.class);
        
        // 使用获取到的Bean
        User user = userService.findById(1L);
        redisUtils.set("temp:user:" + user.getId(), user, 300);
    }
}
```

## 8. 配置类自动生效
当引入common模块且存在相应依赖时，以下配置会自动生效：
- RedisTemplate自动配置（条件加载，仅在Redis依赖存在时）
- RestTemplate自动配置  
- RequestBody重复读取支持

> ⚠️ **注意**：CORS跨域配置建议在网关层统一管理，避免重复配置

无需额外配置，开箱即用！