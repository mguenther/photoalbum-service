FROM java:8-jre
ADD target/photoalbum-webapp-0.1.0.jar /data/photoalbum-webapp-0.1.0.jar
ADD target/classes/config.yml /data/config.yml
CMD java -jar /data/photoalbum-webapp-0.1.0.jar server /data/config.yml
EXPOSE 8080
