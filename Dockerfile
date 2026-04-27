FROM gradle:9.4.1-jdk21-alpine AS build
WORKDIR /workspace

COPY settings.gradle build.gradle ./
RUN gradle --no-daemon dependencies

COPY src src
RUN gradle --no-daemon clean bootJar -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=build /workspace/build/libs/*.jar app.jar

ENV TZ=UTC
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=25.0 -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget -qO- http://127.0.0.1:8081/actuator/health | grep -q "\"status\":\"UP\"" || exit 1

USER spring:spring
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
