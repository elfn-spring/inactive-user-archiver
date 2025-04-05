# 🗃️ inactive-user-archiver

## 🧰 Application Spring Boot + Spring Batch pour l'archivage automatique des utilisateurs inactifs

Cette application exécute un **traitement batch quotidien** pour détecter et **archiver les utilisateurs inactifs depuis plus de 90 jours**, à l’aide d’un `CronJob` Kubernetes.

---

### 🧾 Fonctionnalités principales

- 📆 **Planification automatique** via `CronJob` Kubernetes
- 🧼 **Archivage** des utilisateurs dans une table dédiée `archived_users`
- 🗑️ **Suppression** des utilisateurs archivés de la table `users`
- ⚙️ **Spring Batch** avec `JdbcCursorItemReader` et `CompositeItemWriter`
- 🐳 Déployable sous forme de **pod batch éphémère**

---

### 🛠️ Stack technique

| Composant     | Description                        |
|---------------|------------------------------------|
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="24"/> Spring Boot | Application Java |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="24"/> Spring Batch | Orchestration du traitement batch |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="24"/> PostgreSQL / H2 | Base de données relationnelle |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/kubernetes/kubernetes-plain.svg" width="24"/> Kubernetes | Orchestration des pods/applications |
| <img src="https://img.icons8.com/fluency/48/cron-job.png" width="24"/> CronJob K8s | Déclencheur automatique quotidien |


---

### 📊 Diagramme de flux (Mermaid)

```mermaid
flowchart TD
subgraph Kubernetes_Cluster
K8sCronJob[Kubernetes CronJob Planification : 1 fois par jour]
K8sCronJob --> PodBatch[📦 Pod Batch éphémère]

Pod1[🟦 Pod App 1]
Pod2[🟦 Pod App 2]
Pod3[🟦 Pod App 3]
end

PodBatch --> SpringBoot[☕ Spring Boot App]
SpringBoot --> SpringBatch[⚙️ Spring Batch Job]
SpringBatch --> DB1[(📂 Table users)]
SpringBatch --> DB2[(📦 Table archived_users)]

---

src/
└── main/
    ├── java/com/example/batch/
    │   ├── BatchApplication.java        # 🚀 Point d'entrée de l'application Spring Boot
    │   ├── config/BatchConfig.java      # ⚙️ Configuration Spring Batch (étapes, lecteurs, writers, job)
    │   ├── job/ArchiveInactiveUsersJob.java  # 📦 Définition du job batch
    │   └── model/
    │       ├── User.java                # 👤 Entité représentant un utilisateur actif
    │       └── ArchivedUser.java        # 🗃️ Entité représentant un utilisateur archivé
    └── resources/
        ├── application.yml              # ⚙️ Configuration de l'application Spring Boot
        └── schema.sql                   # 🗄️ Script SQL pour créer les tables (H2/PostgreSQL)
