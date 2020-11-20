FROM maven:3.6.3-jdk-11
ADD . /backend
WORKDIR /backend
RUN mvn install
RUN mv /backend/target/project-backend--*.jar /backend/backend.jar
WORKDIR /backend
CMD ["java", "-jar", "./backend.jar"]
