# 隐性业务约定

## API 端点

```
POST /docker/pull     Body: { "imageName": "...", "imageTag": "..." }   # 拉取 Docker 镜像
POST /docker/search   Body: { "imageName": "..." }                      # 搜索 Docker 镜像
POST /system/test     Body: { "username": "...", "password": "..." }    # 系统认证测试
```

## 关键业务流程

**镜像拉取流程**: 接收镜像名+标签（默认 latest）→ 校验参数 → 重启 Clash 代理（用于访问 Docker Hub）→ 通过 TCP 连接 Docker 守护进程 → 拉取镜像 → 返回结果

## 数据库

- 数据库名: `dipp_agent`，表: `sys_config`
- 配置项通过 `SysConfigEnum` 枚举定义 key，包括: `docker.ip`、`docker.port`、`clash.shellPath`、`sys.status`、`sys.username`、`sys.password`

## 代码约定

- 依赖注入使用 `@Resource`（JSR-250），非 `@Autowired`
- 实体类使用 Lombok `@Data` 注解
- MyBatis-Plus 表映射使用 `@TableName`、`@TableId`、`@TableField` 注解
- Controller 中不使用 `@Transactional`

## 注意事项

- Docker 守护进程通过 TCP 连接（IP 和端口从数据库 `sys_config` 表读取，非本地 socket）
- `DockerImagesLogic.packImageToTar()` 当前为空实现，尚未完成
