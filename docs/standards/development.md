## 构建命令
mvn clean package -DskipTests  # 构建项目
mvn test                       # 运行测试
mvn spotless:apply             # 代码格式化

## 质量标准
- 新增代码必须包含单元测试，覆盖率 > 80%。
- 禁止在 Controller 层使用 `@Transactional`。