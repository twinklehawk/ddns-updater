FROM gcr.io/distroless/java21-debian12:nonroot

LABEL org.opencontainers.image.title=ddns-updater
LABEL org.opencontainers.image.url=https://github.com/twinklehawk/ddns-updater
LABEL org.opencontainers.image.source=https://github.com/twinklehawk/ddns-updater
LABEL org.opencontainers.image.description="DDNS Updater"
LABEL org.opencontainers.image.licenses=Apache-2.0

WORKDIR /app
COPY build/libs/app.jar app.jar

CMD ["app.jar"]
