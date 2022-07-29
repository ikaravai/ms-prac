FROM openjdk:17-alpine

RUN apk --no-cache add curl

ENV TZ=Europe/Minsk
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ARG JAR_FILE=build/libs/config-service-0.0.1-SNAPSHOT.jar
ARG JAR_LIB_FILE=build/libs

WORKDIR /user/local/runme

COPY ${JAR_FILE} myconfserver.jar

ADD ${JAR_LIB_FILE} lib/

EXPOSE 8071
ENTRYPOINT ["java", "-jar", "myconfserver.jar"]
