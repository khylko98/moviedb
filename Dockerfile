FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} moviedb.jar
ENTRYPOINT ["java","-jar","/moviedb.jar"]