FROM openjdk:17-alpine as build
WORKDIR application
ARG JAR_FILE=build/libs/mybatis-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM openjdk:17-alpine
VOLUME /tmp
ENV TZ=Europe/Minsk
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
WORKDIR application
COPY --from=build application/dependencies/ ./
COPY --from=build application/spring-boot-loader/ ./
COPY --from=build application/snapshot-dependencies/ ./
COPY --from=build application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
#FROM openjdk:17-alpine
#
#RUN apk --no-cache add curl
#
#ENV TZ=Europe/Minsk
#RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#
#ARG JAR_FILE=build/libs/license-service-0.0.1-SNAPSHOT.jar
#ARG JAR_LIB_FILE=build/libs
#
#WORKDIR /user/local/runme
#
#COPY ${JAR_FILE} mylicservice.jar
#
#ADD ${JAR_LIB_FILE} lib/
#
#EXPOSE 8090
#ENTRYPOINT ["java", "-jar", "mylicservice.jar"]






