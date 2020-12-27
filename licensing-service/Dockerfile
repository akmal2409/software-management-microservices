#stage 1
#Start with a base image containing JVM
FROM openjdk:11-slim as build

# ADD Maintainer
LABEL maintainer="Akmal Alikhujaev <akmalalikhujaev@gmail.com>"

# The application's jar file
ARG JAR_FILE

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

# Unpackage jar file
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)


# stage 2
# Same JVM
FROM openjdk:11-slim

# Add volume /tmp
VOLUME /tmp

#You install nc (netcat), a utility that you’ll use to ping dependent services to see if they are up.
RUN apt-get update && apt-get -y install netcat && apt-get clean

# COPY unpackaged application to the new container
ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# execute application
ENTRYPOINT ["java","-cp","app:app/lib/*","tech.talci.licensingservice.LicensingServiceApplication"]
