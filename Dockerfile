ARG username
ARG password
ARG url
ENV username=$username
ENV password=$password
ENV url=$url
FROM maven:3.6.3-jdk-11
ADD . /backend
WORKDIR /backend
RUN mvn install
RUN mv /backend/target/project-backend--*.jar /backend/backend.jar
WORKDIR /backend
RUN echo $username
RUN echo $password
RUN echo $url
CMD ["java", "-jar", "./backend.jar"]
