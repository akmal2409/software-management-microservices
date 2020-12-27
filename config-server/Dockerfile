FROM openjdk:11-slim as build

LABEL maintainer="Akmal Alikhujaev <akmalalikhujaev@gmail.com>"

ARG JAR_FILE

COPY ${JAR_FILE} app.jar

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)

FROM openjdk:11-slim

ARG DEPENDENCY=/target/dependency

VOLUME /tmp

RUN apt-get update && apt-get -y install netcat && apt-get clean

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","tech.talci.configserver.ConfigServerApplication"]
