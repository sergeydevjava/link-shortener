FROM amazoncorretto:17.0.7-alpine

COPY ./build/libs/link-shortener-1.0-SNAPSHOT.jar ./link-shortener.jar

ENV TZ=Europe/Moscow

EXPOSE 8080

CMD ["java", "-jar", "./link-shortener.jar"]

