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


## 📂 Structure du projet

Voici l’organisation des fichiers du projet `inactive-user-archiver` :

![ChatGPT Image 5 avr  2025, 02_15_01](https://github.com/user-attachments/assets/bb7e9ba5-450c-4efd-a8a0-a6c351173f2f)
<img src="https://github.com/user/repo/assets/123456/structure.png" alt="Structure du projet" width="500"/>


---

## 📊 Diagramme de flux (Mermaid)

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
