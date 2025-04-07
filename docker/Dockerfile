# Étape 1 : Utiliser une image officielle avec JDK 21
FROM eclipse-temurin:21-jdk

# Étape 2 : Copier le JAR depuis le répertoire parent
COPY ../target/inactive-user-archiver-0.0.1-SNAPSHOT.jar app.jar

# ✅ Copie les fichiers de configuration et ressources (ex : schema.sql, application-dev.yml) dans l’image
COPY ../src/main/resources /app/resources

# Étape 3 : Lancer l'application avec :
# - Console H2 autorisée à distance
# - Activation du profil via variable d'environnement SPRING_PROFILE (par défaut "dev")
ENV SPRING_PROFILE=dev


# ▶️ Point d'entrée de l'application :
# - Active l’accès distant à la console H2 (nécessaire en Docker)
# - Spécifie le fichier de configuration Spring externe à utiliser
# - Lance le fichier .jar de l’application

ENTRYPOINT ["java", "-Dh2.console.settings.web-allow-others=true", "-Dspring.config.location=file:/app/resources/application-dev.yml", "-jar", "/app.jar"]

