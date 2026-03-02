# Common公共模块配置说明

## 概述
此模块提供所有微服务共享的公共配置文件`application-common.yml`，包含：
- Redis通用配置
- Jackson序列化配置
- 服务器性能配置
- 监控端点配置
- 日志标准化配置
- 公共工具参数配置

## 使用方式

### 1. 在微服务中引入common模块
```xml
<dependency>
    <groupId>com.cobazaar</groupId>
    <artifactId>common</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 2. 在微服务的application.yml中引入公共配置
```yaml
spring:
  profiles:
    include: common
```

或者在bootstrap.yml中：
```yaml
spring:
  application:
    name: your-service-name
  profiles:
    include: common
```

### 3. 覆盖特定环境配置
可以在各个环境的配置文件中覆盖公共配置：

**application-dev.yml**（开发环境）：
```yaml
spring:
  redis:
    host: 192.168.1.100  # 覆盖公共配置中的Redis地址
    password: dev123     # 开发环境Redis密码
```

**application-prod.yml**（生产环境）：
```yaml
spring:
  redis:
    host: prod-redis.cluster.local  # 生产环境Redis集群地址
    password: ${REDIS_PASSWORD}     # 通过环境变量注入密码
  jackson:
    serialization:
      write-null-map-values: true   # 生产环境可能需要不同的序列化规则
```

## 配置项说明

### Redis配置
- **基础连接**：host、port、database、timeout
- **连接池**：max-active、max-idle、min-idle、max-wait
- **Redisson**：连接池、超时、重试参数
- **环境隔离**：通过prefix实现多环境数据隔离

### 性能配置
- **Tomcat线程池**：max-threads、min-spare-threads等
- **连接限制**：max-connections、accept-count等

### 监控配置
- **Actuator端点**：health、info、metrics等
- **安全控制**：show-details权限控制

### 日志配置
- **标准化格式**：统一的时间戳、线程、级别格式
- **级别控制**：合理的默认日志级别
- **组件过滤**：对第三方组件的日志级别优化

## 最佳实践

1. **环境隔离**：使用Redis环境前缀区分不同环境数据
2. **配置覆盖**：在具体服务中只覆盖必要的配置项
3. **安全敏感**：密码等敏感信息通过环境变量注入
4. **性能调优**：根据服务特性调整线程池和连接池参数
5. **监控完善**：启用必要的监控端点便于运维

## 注意事项

- 公共配置提供的是**推荐值**，各服务可根据实际需求调整
- 敏感配置（如密码）建议通过环境变量或配置中心管理
- 大型服务可能需要调整线程池和连接池参数
- 生产环境建议启用更严格的日志级别控制