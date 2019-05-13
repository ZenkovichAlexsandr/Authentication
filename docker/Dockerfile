FROM java:8-jre
MAINTAINER Alexander Zenkovich <zenkovich.alexsandr@gmail.com>

ADD implementation/target/implementation-*.jar app.jar

CMD ["java","-jar", "/app.jar"]
EXPOSE 8080