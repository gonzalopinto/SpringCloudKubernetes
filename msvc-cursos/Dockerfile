FROM openjdk:18-jdk as builder

WORKDIR /app/msvc-cursos

COPY ./pom.xml /app
COPY ./msvc-cursos/.mvn ./.mvn
COPY ./msvc-cursos/mvnw .
COPY ./msvc-cursos/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

#Esta hace lo mismo que la de arriba, pero es mejor la instruccion de arriba
#RUN ./mvnw dependency:go-offline

COPY ./msvc-cursos/src ./src

RUN ./mvnw clean package -DskipTests  


FROM openjdk:18-jdk

WORKDIR /app
RUN mkdir ./logs
COPY --from=builder /app/msvc-cursos/target/msvc-cursos-0.0.1-SNAPSHOT.jar .

EXPOSE 8001
# CMD sleep 10 && java -jar msvc-cursos-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "msvc-cursos-0.0.1-SNAPSHOT.jar"]