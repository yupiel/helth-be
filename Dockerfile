FROM adoptopenjdk/openjdk15:x86_64-alpine-jre-15.0.2_7
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY /build/libs/*.jar /app.jar
CMD ["sh", "-c", "java -Dserver.port=$PORT -Dspring.profiles.active=$PROFILE -Dspring.datasource.url=jdbc:postgresql://helth-postgresql:5432/${POSTGRES_DB}$POSTGRES_DB -Dspring.datasource.username=$POSTGRES_USER -Dspring.datasource.password=$POSTGRES_PASSWORD -jar /app.jar"]