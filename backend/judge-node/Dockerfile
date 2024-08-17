FROM openjdk:17

# 复制您的应用程序到容器中
WORKDIR /usr/judge-node
COPY ./target/judge-node-0.0.1-SNAPSHOT.jar .

# 如果需要，设置环境变量、暴露端口等
# ENV MY_ENV_VAR=example_value
EXPOSE 8001

# 启动您的应用程序
CMD ["java", "-jar", "judge-node-0.0.1-SNAPSHOT.jar"]