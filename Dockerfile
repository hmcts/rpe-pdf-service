FROM hmcts/cnp-java-base:openjdk-jre-8-alpine-1.4

# Mandatory!
ENV APP pdf-service.jar
ENV APPLICATION_TOTAL_MEMORY 1024M
ENV APPLICATION_SIZE_ON_DISK_IN_MB 66

COPY build/libs/$APP /opt/app/

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD http_proxy="" wget -q --spider http://localhost:5500/health || exit 1

EXPOSE 5500