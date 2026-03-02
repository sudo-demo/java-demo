# Knife4j 迁移指南

本文档记录了从 Springfox Swagger 2.x 到 Knife4j 4.x 的完整迁移过程。

## 迁移概览

### 从什么迁移到什么？
- **原技术栈**：Springfox Swagger 2.9.2 + Spring Boot 2.7.9
- **新技术栈**：Knife4j 4.3.0 + SpringDoc OpenAPI 3 + Spring Boot 2.7.9

### 主要变化

| 方面 | 原配置 | 新配置 |
|------|--------|--------|
| 核心依赖 | springfox-swagger2 | springdoc-openapi-ui + knife4j-openapi3-spring-boot-starter |
| 配置类 | SpringDocConfig (@EnableSwagger2) | Knife4jConfig (基于OpenAPI 3) |
| 访问路径 | /swagger-ui.html | /doc.html (增强版) 和 /swagger-ui.html (原生) |
| API文档路径 | /v2/api-docs | /v3/api-docs |

## 兼容性说明

### 注解兼容性 ✅
现有的 Swagger 2.x 注解完全兼容：
- `@Api` → 继续使用
- `@ApiOperation` → 继续使用  
- `@ApiParam` → 继续使用
- `@ApiModelProperty` → 继续使用

### 代码无需修改 ✅
所有现有的 Controller、Entity、DTO 代码保持不变

## 配置变更详情

### 1. Maven 依赖变更

**删除的依赖：**
```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
    <version>3.0.3</version>
</dependency>
```

**新增的依赖：**
```xml
<!-- SpringDoc OpenAPI 3 (Knife4j 基础依赖) -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.6.14</version>
</dependency>

<!-- Knife4j Spring Boot Starter (最新版本，基于SpringDoc) -->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi3-spring-boot-starter</artifactId>
    <version>4.3.0</version>
</dependency>
```

### 2. 配置文件变更

**新增配置项 (application-common.yml)：**

```yaml
# SpringDoc OpenAPI 3 配置
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  packages-to-scan: com.cobazaar
  paths-to-match: /**
  default-produces-media-type: application/json
  default-consumes-media-type: application/json

# Knife4j 增强配置
knife4j:
  enable: true
  production: false
  basic:
    enable: false
    username: admin
    password: admin
  setting:
    enable-search: true
    enable-group: true
    enable-version: true
    enable-author: true
    enable-debug: true
    enable-request-cache: true
    enable-param-filter: true
    enable-host: false
    enable-host-text: localhost:8080
  cors: false
```

### 3. 配置类变更

**删除的类：**
- `SpringDocConfig.java` (Springfox 配置)
- `SwaggerResource.java` (网关兼容性类)
- `SwaggerResourceConfig.java` (网关配置)
- `SwaggerResourcesConfig.java` (网关资源配置)
- `SwaggerResourcesProvider.java` (网关资源提供者)
- `SwaggerResourcesController.java` (网关控制器)

**新增的类：**
- `Knife4jConfig.java` (基于OpenAPI 3的新配置)

## 访问地址变更

### 新的访问路径

1. **Knife4j 增强文档界面** (推荐)：
   ```
   http://localhost:9999/doc.html
   ```

2. **原生 Swagger UI**：
   ```
   http://localhost:9999/swagger-ui.html
   ```

3. **OpenAPI 3 JSON 文档**：
   ```
   http://localhost:9999/v3/api-docs
   ```

## 新增功能特性

### Knife4j 4.x 增强功能 ✨

1. **现代化UI界面** - 更美观、更易用的文档界面
2. **在线调试功能** - 直接在文档界面测试API
3. **接口搜索** - 快速查找特定接口
4. **参数缓存** - 自动缓存常用参数值
5. **文档分组** - 支持按业务模块分组展示
6. **离线文档** - 支持导出HTML、Markdown等格式
7. **响应式设计** - 适配移动端浏览
8. **主题切换** - 支持多种界面主题

## 验证步骤

### 1. 构建项目
```bash
mvn clean install
```

### 2. 启动服务
```bash
cd gateway
mvn spring-boot:run
```

### 3. 验证访问
- 访问 `http://localhost:9999/doc.html`
- 确认API文档正常显示
- 测试在线调试功能

### 4. 运行测试
```bash
mvn test -pl common -Dtest=Knife4jConfigTest
```

## 注意事项

### 生产环境部署

在生产环境中，建议关闭文档访问：

```yaml
knife4j:
  production: true  # 设置为true关闭文档访问
```

或者通过环境变量控制：
```bash
-Dknife4j.production=true
```

### 安全考虑

如果需要保护文档访问，可以启用基础认证：

```yaml
knife4j:
  basic:
    enable: true
    username: your-username
    password: your-password
```

## 故障排除

### 常见问题

1. **文档页面空白**
   - 检查依赖是否正确引入
   - 确认配置文件中 `knife4j.enable=true`

2. **接口不显示**
   - 检查 `springdoc.packages-to-scan` 配置
   - 确认Controller类上有正确的注解

3. **路径404错误**
   - 确认访问路径正确 (`/doc.html` 而不是 `/swagger-ui.html`)
   - 检查网关路由配置

### 日志调试

启用详细日志：
```yaml
logging:
  level:
    org.springdoc: DEBUG
    com.github.xiaoymin: DEBUG
```

## 后续优化建议

1. **逐步迁移到OpenAPI 3注解** - 虽然兼容性很好，但建议逐步使用新的注解
2. **添加API版本管理** - 利用Knife4j的分组功能
3. **集成文档自动化** - 结合CI/CD自动发布文档
4. **性能监控** - 监控文档访问性能

---
**迁移完成时间**：2026-02-08  
**负责人**：Cobazaar开发团队