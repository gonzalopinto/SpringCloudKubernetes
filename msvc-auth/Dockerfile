#aqui se define la global
ARG MSVC_NAME=msvc-auth

FROM openjdk:18-jdk as builder

#hay que declararla en cada etapa despues del FROM
ARG MSVC_NAME

WORKDIR /app/$MSVC_NAME
COPY ./pom.xml /app
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .
COPY ./$MSVC_NAME/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

#Esta hace lo mismo que la de arriba, pero es mejor la instruccion de arriba
#RUN ./mvnw dependency:go-offline

COPY ./$MSVC_NAME/src ./src

RUN ./mvnw clean package -DskipTests  

FROM openjdk:18-jdk

WORKDIR /app
RUN mkdir ./logs

#hay que declararla en cada etapa despues del FROM
ARG MSVC_NAME
ARG TARGET_FOLDER=/app/$MSVC_NAME/target
COPY --from=builder $TARGET_FOLDER/msvc-auth-0.0.1-SNAPSHOT.jar .
ARG PORT_APP=9000
ENV PORT $PORT_APP
EXPOSE $PORT

CMD ["java", "-jar", "msvc-auth-0.0.1-SNAPSHOT.jar"]