# System模块基础架构说明

## 架构概述
System模块采用标准的六层架构设计，基于Spring Boot + MyBatis-Plus构建。

## 目录结构
```
com.cobazaar.system
├── config/                 # 配置类
│   ├── SystemConfig.java          # 基础配置
│   └── SystemBusinessConfig.java  # 业务配置
├── constant/               # 常量定义
│   └── SystemConstants.java
├── controller/             # 控制层
│   ├── base/              # 基础控制器
│   │   └── SystemBaseController.java
│   └── UserController.java
├── dto/                   # 数据传输对象
│   └── user/             # 用户相关DTO
│       ├── UserCreateDTO.java
│       ├── UserQueryDTO.java
│       └── UserUpdateDTO.java
├── entity/                # 实体类
│   └── User.java
├── enums/                 # 枚举类
│   └── SystemEnums.java
├── exception/             # 异常处理
│   └── SystemGlobalExceptionHandler.java
├── mapper/                # 数据访问层
│   ├── SystemBaseMapper.java      # 基础Mapper接口
│   ├── impl/                     # Mapper实现
│   │   └── SystemBaseMapperImpl.java
│   └── UserMapper.java
├── service/               # 服务层
│   ├── SystemBaseService.java     # 基础服务接口
│   ├── UserService.java
│   └── impl/                     # 服务实现
│       ├── SystemBaseServiceImpl.java
│       └── UserServiceImpl.java
├── util/                  # 工具类
│   └── SystemUtils.java
├── vo/                    # 视图对象
│   └── user/             # 用户相关VO
│       └── UserVO.java
└── SystemApplication.java # 启动类
```

## 核心组件说明

### 1. 基础架构组件
- **SystemBaseMapper**: 扩展MyBatis-Plus的BaseMapper，提供通用数据库操作
- **SystemBaseService**: 扩展MyBatis-Plus的IService，提供通用业务操作
- **SystemBaseController**: 抽象控制器，提供CRUD操作模板方法

### 2. 分层设计
- **Controller层**: 处理HTTP请求，参数验证，调用Service层
- **Service层**: 业务逻辑处理，事务管理
- **Mapper层**: 数据库操作，SQL映射
- **Entity层**: 数据实体，ORM映射
- **DTO/VO层**: 数据传输对象和视图对象

### 3. 特性
- 继承common模块的基础功能
- 支持批量操作
- 统一异常处理
- 标准化的CRUD操作模板
- 可扩展的架构设计

## 使用说明
1. 业务开发时继承相应的Base类
2. 在对应层添加具体的业务实现
3. 遵循六层架构规范
4. 复用common模块提供的通用功能

## 待完善内容
- 具体业务逻辑实现
- 自定义查询方法
- 详细的异常处理
- 完整的单元测试