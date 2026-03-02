# Common 模块说明

## 模块介绍
Common模块是整个微服务架构的公共基础组件库，提供通用的配置、工具类、常量定义等基础功能。

## 包结构说明

### config 包 - 配置类
- `RedisConfig.java` - Redis配置类，提供RedisTemplate的自动配置
- `RestTemplateConfig.java` - RestTemplate自动配置类
- `WebMvcConfig.java` - WebMvc配置类，包含请求包装过滤器（CORS配置建议在网关统一管理）
- `MybatisPlusConfig.java` - MyBatis Plus配置类，提供分页插件等核心功能

### constant 包 - 常量定义
- `Constants.java` - 系统常量接口，包含HTTP、Redis、业务等相关常量

### enums 包 - 枚举类
- `ResponseEnum.java` - 响应状态枚举，定义标准的HTTP响应状态

### exception 包 - 异常类
- `BizException.java` - 业务异常类，统一异常处理

### result 包 - 响应结果
- `Result.java` - 统一响应结果封装类

### utils 包 - 工具类
- `RedisUtils.java` - Redis缓存工具类，基于RedisTemplate操作
- `RedissonUtils.java` - Redisson工具类，提供分布式锁、原子计数器等高级功能
- `SpringContextHolder.java` - Spring上下文工具类，用于获取Spring Bean

> **工具类使用建议**: 
> - 对于Hutool已提供的基础功能，建议直接使用Hutool原生工具类
> - 集合操作：`CollUtil.isEmpty()`、`CollUtil.newArrayList()`
> - 字符串处理：`StrUtil.isBlank()`、`StrUtil.format()`
> - 日期处理：`DateUtil.format()`、`DateUtil.parse()`
> - 这样可以避免不必要的封装层，提高代码简洁性

### wrapper 包 - 包装类
- `RequestWrapper.java` - Request包装类，允许request body重复读取
- `RequestBodyWrapperFilter.java` - RequestBody包装过滤器
- `HttpHelper.java` - HTTP工具类，提供获取请求Body和真实IP等方法

## 主要功能

### 1. Redis操作
```java
@Autowired
private RedisUtils redisUtils;

// 设置缓存
redisUtils.set("key", "value", 3600);

// 获取缓存
String value = (String) redisUtils.get("key");

// 删除缓存
redisUtils.del("key");
```

### 2. 统一响应
```java
// 成功响应
Result.success(data);

// 失败响应
Result.fail("错误信息");

// 自定义响应
Result.success("操作成功", data);
```

### 3. 异常处理
```java
// 抛出业务异常
throw new BizException("业务错误");

// 使用枚举
throw new BizException(ResponseEnum.PARAM_ERROR);
```

### 4. 常量使用
```java
// 使用HTTP常量
String authHeader = Constants.Http.HEADER_AUTHORIZATION;

// 使用Redis常量
String userKey = Constants.Redis.PREFIX_USER + userId;
```

### 5. Redisson分布式锁
```java
@Autowired
private RedissonUtils redissonUtils;

// 获取分布式锁
RLock lock = redissonUtils.getLock("myLock");

// 尝试获取锁（等待3秒，持有10秒）
if (redissonUtils.tryLock("myLock", 3, 10, TimeUnit.SECONDS)) {
    try {
        // 执行需要同步的代码
        doSomething();
    } finally {
        // 释放锁
        redissonUtils.unlock("myLock");
    }
}

// 原子计数器操作
long counter = redissonUtils.incrementAndGet("myCounter");
counter = redissonUtils.decrementAndGet("myCounter");
```

## 使用说明

1. 在需要使用的模块中添加common依赖
2. 直接使用相关工具类和配置
3. 统一使用Result进行接口响应
4. 使用BizException进行异常处理
5. 通过Constants访问系统常量

## 注意事项

- 该模块设计为无循环依赖的基础组件库
- 所有工具类均为静态方法或Spring Bean
- RedisUtils和RedissonUtils都需要Redis服务支持
- Request包装功能需要Web环境支持
- Redisson提供分布式锁、原子操作等高级功能
- MyBatis Plus需要配置数据源和相应的数据库驱动
- 代码生成器需要根据实际数据库连接信息调整配置
- BaseEntity中的用户ID自动填充需要结合具体的用户认证体系实现
- 数据库连接信息：127.0.0.1:3307，用户名密码均为root
- 测试表结构SQL文件位于：`src/main/resources/sql/test-user-schema.sql`
- 完整数据库初始化脚本：`src/main/resources/sql/create-demo-database.sql`

## 数据库初始化

### Docker MySQL 初始化与升级（推荐）
```bash
# 1. 首次初始化（全新安装）
./init-db-with-third-auth.sh

# 2. 增量升级（已有数据库，保护数据）
./upgrade-db-for-third-auth.sh

# 3. 通用升级框架
./db-upgrade-framework.sh [容器名] [密码] [端口] [主机] [数据库]

# 4. 配置向导（交互式配置）
./config-mysql.sh

# 5. 通用初始化脚本
./init-docker-db.sh

# 6. 手动执行（如果知道容器信息）
docker exec -i mysql8 mysql -uroot -proot < common/src/main/resources/sql/create-demo-database.sql
```

### 传统 MySQL 初始化
```bash
# Linux/Mac 系统
./create-demo-db.sh

# Windows 系统
create-demo-db.bat

# 或者手动执行SQL
mysql -h 127.0.0.1 -P 3307 -u root -p < common/src/main/resources/sql/create-demo-database.sql
```

### 初始化内容
- **数据库名**：`demo`
- **默认账号**：
  - 管理员：`admin` / `admin123` (工号: EMP001)
  - 测试用户：`test` / `admin123` (工号: EMP002)
  - 开发者：`developer` / `admin123` (工号: EMP003)
- **增强表结构**：
  - `sys_dept`：部门表
  - `sys_user`：用户表（含工号、登录限制字段）
  - `sys_user_third_auth`：第三方认证绑定表（微信、飞书等）
  - `sys_login_log`：登录日志表
  - `sys_role`：角色表
  - `sys_menu`：菜单表
  - `sys_user_role`：用户角色关联表
  - `sys_role_menu`：角色菜单关联表
- **核心特性**：
  - UTF8MB4字符集支持，解决中文乱码
  - 全面的索引优化，提升查询性能
  - 雪花算法主键设计，保证全局唯一
  - 第三方认证集成（微信、企业微信、飞书、钉钉）
  - 用户登录失败次数限制
  - 完整的登录日志记录
  - 工号唯一性约束

### 配置文件
- `mysql-config.env`：存储 MySQL 连接配置
jia- `init-db-with-third-auth.sh`：首次初始化脚本（全新安装）
- `upgrade-db-for-third-auth.sh`：增量升级脚本（保护现有数据）
- `db-upgrade-framework.sh`：通用升级框架
- `init-docker-db.sh`：通用 Docker 环境数据库初始化脚本
- `config-mysql.sh`：交互式配置向导

### 使用建议
根据不同场景选择合适的脚本：

**全新安装场景**：
```bash
./init-db-with-third-auth.sh
```

**已有数据库升级场景**：
```bash
./upgrade-db-for-third-auth.sh
```

**通用升级框架**：
```bash
./db-upgrade-framework.sh mysql8 root 3307 127.0.0.1 demo
```

所有脚本都具有以下特点：
- ✅ 增量更新，不删除现有数据
- ✅ 字段存在性检查，避免重复添加
- ✅ UTF8MB4字符集支持
- ✅ 完善的索引优化
- ✅ 雪花算法主键设计