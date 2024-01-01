FROM openjdk:17
ENV MYSQL_DATABASE: nabd
ENV MYSQL_HOST: 172.30.121.217
ENV MYSQL_PASSWORD: admin
ENV MYSQL_PORT: 3306
ADD target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]