FROM openjdk:8-jre

COPY build/install/pdf-service /opt/app/

WORKDIR /opt/app

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD http_proxy="" curl --silent --fail http://localhost:5501/health

EXPOSE 5500 5501

ENTRYPOINT ["/opt/app/bin/pdf-service"]

