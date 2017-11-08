FROM openjdk:8-jre-alpine

WORKDIR /opt/app

COPY build/install/pdf-service /opt/app/

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD http_proxy="" curl --silent --fail http://localhost:5500/health

EXPOSE 5500

ENTRYPOINT ["/opt/app/bin/pdf-service"]

