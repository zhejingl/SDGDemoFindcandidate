#FROM fabric8/java-jboss-openjdk8-jdk:1.5.2
#ENV JAVA_APP_DIR=/deployments
#ENV JAEGER_SERVICE_NAME=customer\
#  JAEGER_ENDPOINT=http://jaeger-collector.istio-system.svc:14268/api/traces\
#  JAEGER_PROPAGATION=b3\
#  JAEGER_SAMPLER_TYPE=const\
#  JAEGER_SAMPLER_PARAM=1
#EXPOSE 8080 8778 9779
#COPY target/customer.jar /deployments/


FROM openjdk:8-jdk-alpine
LABEL maintainer="kenny.j.yang@gmail.com"
# Add a volume pointing to /tmp
VOLUME /tmp
# Make port 8080 available to the world outside this container
EXPOSE 8080 8778 9779

# The application's jar file
ARG JAR_FILE=target/SDGDemoFindcandidate-0.0.1.jar

# Add the application's jar to the container
ADD ${JAR_FILE} SDGDemoFindcandidate-0.0.1.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/SDGDemoFindcandidate-0.0.1.jar"]
