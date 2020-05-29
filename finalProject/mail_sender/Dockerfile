FROM openjdk:11-slim

RUN mkdir /app
COPY build/libs/*.jar ./app/

EXPOSE 8080

WORKDIR /app
ENTRYPOINT java -jar *.jar