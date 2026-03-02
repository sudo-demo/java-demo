# 响应状态码使用指南

## 业务状态码规范

### 状态码分类
- **0**: 成功 (SUCCESS)
- **1**: 通用失败 (FAIL)
- **1001-1999**: 系统级错误
- **2001-2999**: 业务级错误

### 完整状态码列表
```java
SUCCESS(0, "success")                    // 成功
FAIL(1, "fail")                          // 失败
PARAM_ERROR(1001, "参数错误")             // 参数错误
UNAUTHORIZED(1002, "未授权")              // 未授权
FORBIDDEN(1003, "禁止访问")               // 禁止访问
NOT_FOUND(1004, "资源不存在")             // 资源不存在
REQUEST_TIMEOUT(1005, "请求超时")         // 请求超时
INTERNAL_SERVER_ERROR(1006, "服务器内部错误") // 服务器内部错误
BAD_GATEWAY(1007, "网关错误")             // 网关错误
SERVICE_UNAVAILABLE(1008, "服务不可用")    // 服务不可用
GATEWAY_TIMEOUT(1009, "网关超时")         // 网关超时
BUSINESS_ERROR(2001, "业务异常")          // 业务异常
```

## 使用示例

### 1. 基础使用
```java
// 成功响应
Result.success();  // code: 0, message: "success"

// 成功响应带数据
Result.success(user);  // code: 0, message: "success", data: user对象

// 成功响应带自定义消息
Result.success("操作成功", userId);  // code: 0, message: "操作成功", data: userId

// 失败响应
Result.fail();  // code: 1, message: "fail"

// 失败响应带消息
Result.fail("用户名已存在");  // code: 1, message: "用户名已存在"

// 失败响应带状态码
Result.fail(1001, "参数校验失败");  // code: 1001, message: "参数校验失败"
```

### 2. 使用枚举的便捷方法
```java
// 使用预定义的枚举
Result.fail(ResponseEnum.PARAM_ERROR);  // code: 1001, message: "参数错误"
Result.fail(ResponseEnum.UNAUTHORIZED); // code: 1002, message: "未授权"

// 带数据的失败响应
Result.fail(ResponseEnum.NOT_FOUND, "用户ID: " + userId);  // code: 1004, message: "资源不存在"

// 快捷方法
Result.paramError();      // code: 1001, message: "参数错误"
Result.unauthorized();    // code: 1002, message: "未授权"
Result.businessError("库存不足");  // code: 2001, message: "库存不足"
```

### 3. 控制器中的实际应用
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            if (user == null) {
                return Result.fail(ResponseEnum.NOT_FOUND);
            }
            return Result.success(user);
        } catch (BusinessException e) {
            return Result.businessError(e.getMessage());
        } catch (Exception e) {
            log.error("查询用户失败", e);
            return Result.fail(ResponseEnum.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping
    public Result<Long> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            // 参数校验已在@Valid中完成
            Long userId = userService.createUser(request);
            return Result.success("用户创建成功", userId);
        } catch (ValidationException e) {
            return Result.paramError();
        } catch (BusinessException e) {
            return Result.businessError(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public Result<Void> updateUser(@PathVariable Long id, 
                                  @RequestBody UpdateUserRequest request) {
        try {
            // 权限校验
            if (!hasPermission(id)) {
                return Result.unauthorized();
            }
            
            userService.updateUser(id, request);
            return Result.success();
        } catch (BusinessException e) {
            return Result.businessError(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        try {
            if (!isAdmin()) {
                return Result.fail(ResponseEnum.FORBIDDEN);
            }
            
            userService.deleteUser(id);
            return Result.success();
        } catch (BusinessException e) {
            return Result.businessError(e.getMessage());
        }
    }
}
```

### 4. 异常处理器中使用
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.businessError(e.getMessage());
    }
    
    @ExceptionHandler(ValidationException.class)
    public Result<Void> handleValidationException(ValidationException e) {
        return Result.paramError();
    }
    
    @ExceptionHandler(AuthorizationException.class)
    public Result<Void> handleAuthorizationException(AuthorizationException e) {
        return Result.fail(ResponseEnum.UNAUTHORIZED);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public Result<Void> handleResourceNotFoundException(ResourceNotFoundException e) {
        return Result.fail(ResponseEnum.NOT_FOUND);
    }
}
```

### 5. 前端对接示例
```javascript
// JavaScript前端处理示例
fetch('/api/users/123')
    .then(response => response.json())
    .then(result => {
        if (result.code === 0) {
            // 成功处理
            console.log('用户信息:', result.data);
        } else if (result.code === 1001) {
            // 参数错误
            alert('参数错误: ' + result.message);
        } else if (result.code === 1002) {
            // 未授权
            window.location.href = '/login';
        } else if (result.code >= 1000 && result.code < 2000) {
            // 系统错误
            alert('系统错误: ' + result.message);
        } else if (result.code >= 2000) {
            // 业务错误
            alert('业务错误: ' + result.message);
        }
    })
    .catch(error => {
        console.error('请求失败:', error);
    });
```

## 最佳实践

1. **状态码语义化**: 使用有意义的状态码，便于前后端理解
2. **错误信息友好**: 提供清晰的错误描述信息
3. **分类管理**: 按照错误类型分类状态码区间
4. **统一处理**: 在全局异常处理器中统一返回格式
5. **文档化**: 维护状态码文档，便于团队协作

这样的设计既保持了与HTTP状态码的区分，又提供了丰富的业务语义，便于系统维护和问题排查。