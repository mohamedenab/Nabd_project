FROM openjdk:17
ENV MYSQL_DATABASE: nabd
ENV MYSQL_ROOT_PASSWORD: root
ENV MYSQL_HOST: mySql
ENV MYSQL_USER: root
ENV MYSQL_PASSWORD: root
ENV MYSQL_PORT: 3306
ENV MYSQL_DATABASE: nabd
RUN gradlew clean build
ADD target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]