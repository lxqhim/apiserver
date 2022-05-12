FROM adoptopenjdk:11-jre

COPY target/*.jar app.jar

ENTRYPOINT ["java"]
CMD ["-jar","/app.jar"]


# To run postgres container
# docker run -d -p 5432:5432 --name postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=smartbankapp postgres

# docker build -t brainupgrade/sba-apiserver:1.0.0 .
# docker run -d --link postgres:dbserver --name apiserver -e spring.datasource.url=jdbc:postgresql://dbserver:5432/smartbankapp brainupgrade/sba-apiserver:1.0.0