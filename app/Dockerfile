FROM eclipse-temurin:21.0.4_7-jdk@sha256:55725e4e1531b5816284acb94eae597fc668683741ef3514eca9b94f6f7b8696

LABEL org.opencontainers.image.title=ddns-updater
LABEL org.opencontainers.image.url=https://github.com/twinklehawk/ddns-updater
LABEL org.opencontainers.image.source=https://github.com/twinklehawk/ddns-updater
LABEL org.opencontainers.image.description="DDNS Updater"
LABEL org.opencontainers.image.licenses=Apache-2.0

WORKDIR /app
COPY build/libs/app.jar app.jar

# Set user and group
ARG user=ddnsupdater
ARG group=ddnsupdater
ARG uid=1000
ARG gid=1000
RUN groupadd -g ${gid} ${group}
RUN useradd -u ${uid} -g ${group} ${user}
USER ${uid}

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
