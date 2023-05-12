FROM openjdk:17
COPY target/*.jar movies-list.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar","movies-list.jar"]