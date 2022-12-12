# 基础镜像使用Java
FROM java:18
# 作者
MAINTAINER Zhenye
# 将指定目录下的jar包复制到docker容器的/export/Apps/springboot-admin目录下
COPY springboot-admin-0.0.1-SNAPSHOT.jar /export/Apps/springboot-admin/spring-admin-0.0.1-SNAPSHOT.jar
