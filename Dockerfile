ARG APP_INSIGHTS_AGENT_VERSION=3.2.6
FROM hmctspublic.azurecr.io/base/java:17-distroless

# Mandatory!
ENV APP pdf-service-all.jar

COPY lib/applicationinsights.json /opt/app/

EXPOSE 5500

CMD ["pdf-service-all.jar"]
