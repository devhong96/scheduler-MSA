FROM openjdk:17-jdk
COPY ./build/libs/discovery-service.jar discovery-service.jar
ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200", "-jar", "discovery.jar"]