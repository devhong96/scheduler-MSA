FROM openjdk:17-jdk
COPY ./build/libs/article-service.jar article-service.jar
ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200", "-jar", "article-service.jar"]