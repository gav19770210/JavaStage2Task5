FROM openjdk:11
LABEL org.opencontainers.image.authors="agubkin@inno.tech"
LABEL description="Docker image with test task 5."
WORKDIR /app
COPY /target/Stage2Task05-0.0.1-SNAPSHOT.jar /app/Stage2_Task5.jar
ENTRYPOINT ["java","-D\"file.encoding\"=\"UTF-8\"","-jar","Stage2_Task5.jar"]