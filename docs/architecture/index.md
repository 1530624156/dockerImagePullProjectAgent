# 分层架构规则 (至关重要)

```
Controller → Logic → Service → Mapper → Database
               ↓
           Utils（Docker、Process、Date）
```

- **Controller** (`controller/`): REST 入口，处理 HTTP 请求，返回 `RestResult` 统一响应
- **Logic** (`logic/`): 业务逻辑层，所有 Logic 继承 `BaseLogic`，通过 `getSysValue(key)` 读取数据库配置
- **Service** (`service/`): 数据访问服务层，基于 MyBatis-Plus 的 `IService` / `ServiceImpl`
- **Mapper** (`mapper/`): MyBatis 数据库映射接口
- **Utils** (`utils/`): 通用工具类，包含 Docker 操作封装、Shell 命令执行、日期处理等