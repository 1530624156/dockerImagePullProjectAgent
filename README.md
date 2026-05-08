# Docker Images Pull Proxy (DIPP)

一个基于 Spring Boot 的 Docker 镜像拉取代理服务，帮助用户在网络受限环境下通过代理拉取 Docker 镜像并导出为 tar 包。

## 在线体验

**体验地址**: http://dippagent.mavis01.top:88/

## 功能特性

- Docker 镜像拉取代理
- 镜像导出为 tar 包下载
- 邮件通知功能
- 任务管理与状态追踪
- 镜像列表管理
- Clash 代理集成

## 技术栈

- **后端**: Spring Boot 2.6.13 + MyBatis-Plus
- **数据库**: MySQL 8.0
- **容器管理**: docker-java
- **工具库**: Hutool、FastJSON

## 项目结构

```
src/main/java/com/mavis/digg_agent/
├── controller/          # REST API 控制器
│   ├── DockerImagesController.java
│   ├── ImageInfoController.java
│   ├── TaskInfoController.java
│   └── SysConfigController.java
├── logic/               # 业务逻辑层
│   ├── DockerImagesLogic.java
│   ├── ImageInfoLogic.java
│   ├── TaskInfoLogic.java
│   └── ShellLogic.java
├── service/             # 服务层
├── mapper/              # MyBatis Mapper
├── entity/              # 实体类
│   ├── po/              # 持久化对象
│   ├── vo/              # 视图对象
│   ├── param/           # 请求参数
│   └── enums/           # 枚举类
└── utils/               # 工具类
```

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Docker (用于拉取镜像)

### 配置数据库

1. 创建 MySQL 数据库 `dipp_agent`
2. 修改 `src/main/resources/application.properties` 中的数据库配置

### 构建与运行

```bash
# 克隆项目
git clone https://github.com/1530624156/dockerImagePullProjectAgent.git

# 进入项目目录
cd dockerImagePullProjectAgent

# 构建项目
mvn clean package -DskipTests

# 运行项目
java -jar target/digg_agent-0.0.1-SNAPSHOT.jar
```

服务启动后访问: http://localhost:18081

## API 接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/docker/pull` | POST | 提交镜像拉取任务 |
| `/image/list` | GET | 获取可下载镜像列表 |
| `/task/list` | GET | 获取任务列表 |

## 使用方法

1. 在首页输入镜像名称（如 `nginx`、`library/redis`）
2. 可选填入镜像标签（默认 latest）和通知邮箱
3. 点击提交拉取任务
4. 在镜像列表页面下载 tar 包
5. 在目标服务器执行 `docker load -i xxx.tar` 加载镜像

## 赞赏支持

如果这个项目对你有帮助，欢迎请作者喝杯咖啡 ☕

<div align="center">
  <img src="./src/main/resources/static/pic/wx.jpg" width="180" alt="微信赞赏码">
  <img src="./src/main/resources/static/pic/zfb.jpg" width="180" alt="支付宝赞赏码">
</div>

## 作者

- GitHub: [1530624156](https://github.com/1530624156)
- 主页: [Mavis](http://mavis01.top:88/)

## 开源协议

本项目采用 MIT 协议开源，详见 [LICENSE](LICENSE) 文件。
