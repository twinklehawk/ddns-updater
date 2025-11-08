FROM gcr.io/distroless/java21-debian12:nonroot@sha256:5fc28363e84dec4db09b7df71299879039639fa9c13db7c9d36ff61715e10859

LABEL org.opencontainers.image.title=ddns-updater
LABEL org.opencontainers.image.url=https://github.com/twinklehawk/ddns-updater
LABEL org.opencontainers.image.source=https://github.com/twinklehawk/ddns-updater
LABEL org.opencontainers.image.description="DDNS Updater"
LABEL org.opencontainers.image.licenses=Apache-2.0

WORKDIR /app
COPY build/libs/app.jar app.jar

CMD ["app.jar"]
