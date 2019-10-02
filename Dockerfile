ARG APP_INSIGHTS_AGENT_VERSION=2.5.1-BETA
FROM hmctspublic.azurecr.io/base/java:openjdk-8-distroless-1.2

# Mandatory!
ENV APP pdf-service-all.jar

COPY build/libs/$APP /opt/app/
COPY lib/AI-Agent.xml /opt/app/

EXPOSE 5500

CMD ["pdf-service-all.jar"]
