# Étape 1 : Utiliser une image officielle avec JDK 21
FROM eclipse-temurin:21-jdk

# Étape 2 : Copier le JAR depuis le répertoire parent
COPY ../target/inactive-user-archiver-0.0.1-SNAPSHOT.jar app.jar

# Étape 3 : Lancer l'application
ENTRYPOINT ["java", "-jar", "/app.jar"]
