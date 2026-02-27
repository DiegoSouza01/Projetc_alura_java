FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# (O nome exato do arquivo depende do que está no seu pom.xml)
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]