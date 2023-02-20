
FROM openjdk:19-alpine

EXPOSE 5500

ADD target/transferMoneyService-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]