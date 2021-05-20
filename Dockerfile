FROM adoptopenjdk/openjdk15:x86_64-alpine-jre-15.0.2_7
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY /build/libs/*.jar /app.jar
CMD ["sh", "-c", "java -Dserver.port=$PORT -Dspring.profiles.active=$PROFILE -jar /app.jar"]