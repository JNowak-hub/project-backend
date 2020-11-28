FROM maven:3.6.3-jdk-11
ARG username
ARG dbpassword
ARG url
ARG jwtsecret
ENV username=$username
ENV dbpassword=$dbpassword
ENV url=$url
ENV jwtsecret=$jwtsecret
ADD . /backend
WORKDIR /backend
RUN mvn install
RUN mv /backend/target/project-backend-*.jar /backend/backend.jar
WORKDIR /backend
RUN echo $username
RUN echo $dbpassword
RUN echo $url
CMD ["java", "-jar", "./backend.jar"]
