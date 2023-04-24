FROM openjdk:17-jdk-alpine

COPY /target/redis-session-store-*.jar /opt/redis-session-store/

VOLUME /opt/redis-session-store/

WORKDIR /opt/redis-session-store/

EXPOSE 8080

ENTRYPOINT java -jar /opt/redis-session-store/*.jar
