FROM eclipse-temurin:21.0.3_9-jdk@sha256:dd90127baca82663c56ff903156d229a8287fe94a7b0145a31b45fecebdb7be6

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
