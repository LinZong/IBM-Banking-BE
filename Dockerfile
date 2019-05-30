FROM s390x/openjdk:8
WORKDIR /app
VOLUME /tmp
COPY ["target/","."]
RUN ls .
RUN ls /app
ENTRYPOINT ["java","-jar","./IBM-Banking-0.0.1-SNAPSHOT.jar"]
