FROM gcr.io/distroless/java21-debian12:nonroot@sha256:783309675b7508450628dd76bb05b8447b056827835006e58a0c973ab3299ff5

LABEL org.opencontainers.image.title=ddns-updater
LABEL org.opencontainers.image.url=https://github.com/twinklehawk/ddns-updater
LABEL org.opencontainers.image.source=https://github.com/twinklehawk/ddns-updater
LABEL org.opencontainers.image.description="DDNS Updater"
LABEL org.opencontainers.image.licenses=Apache-2.0

WORKDIR /app
COPY build/libs/app.jar app.jar

CMD ["app.jar"]
