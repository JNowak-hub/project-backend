FROM maven:3.6.3-jdk-11
ARG username
ARG dbpassword
ARG url
ARG jwtsecret
ARG clientId
ARG clientSecret
ARG googlesecret
ENV username=$username
ENV dbpassword=$dbpassword
ENV url=$url
ENV jwtsecret=$jwtsecret
ENV clientId=$clientId
ENV clientSecret=$clientSecret
ENV googlesecret=$googlesecret
ADD . /backend
WORKDIR /backend
RUN mvn install
RUN mv /backend/target/project-backend-*.jar /backend/backend.jar
WORKDIR /backend
RUN echo $username
RUN echo $dbpassword
RUN echo $url
CMD ["java", "-jar", "./backend.jar"]
