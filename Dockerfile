FROM gradle:jdk8
LABEL maintainer "qiliang.xiao@daocloud.io"

WORKDIR /home/gradle

COPY . /home/gradle
RUN gradle bootJar
COPY ./build/libs/jobsee-0.0.1-SNAPSHOT.jar app.jar

RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

CMD java -jar app.jar
