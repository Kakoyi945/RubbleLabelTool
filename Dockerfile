# 基础镜像使用Java
FROM java:18
# 作者
MAINTAINER Zhenye
# 将指定目录下的jar包复制到docker容器的/export/Apps/springboot-admin目录下
COPY RubbleLabelTool-0.0.1-SNAPSHOT.jar /export/Apps/springboot-admin/RubbleLabelTool-0.0.1-SNAPSHOT.jar
# 声明服务运行的端口
EXPOSE 8080
# 指定docker容器启动时运行jar包
ENTRYPOINT ["java", "-jar", "/export/Apps/springboot-admin/RubbleLabelTool-0.0.1-SNAPSHOT.jar"]