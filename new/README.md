# rise 后端（new）

## 开始前必读

请先阅读项目总文档：[`项目文档.md`](./项目文档.md)

> 建议每次新任务都先读该文档，再开始开发/联调。

## 项目定位

- 目录：`C:\Users\13798\Desktop\codex\rise\new`
- 角色：后端服务（Spring Boot）
- 对外主要接口：`/vue/**`

## 本地启动（后端）

1. 配置数据库与 `application.properties`/`application.yml`
2. 构建：`mvn clean package`
3. 启动：`java -jar target/rise.war`（或部署外部 Tomcat）

