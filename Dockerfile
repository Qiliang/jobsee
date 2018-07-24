FROM openjdk:8-jdk
LABEL maintainer "qiliang.xiao@daocloud.io"

COPY app.jar app.jar

RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

CMD java -jar app.jar