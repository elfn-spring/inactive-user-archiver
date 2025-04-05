# 🗃️ inactive-user-archiver

## 🧰 Application Spring Boot + Spring Batch pour l'archivage automatique des utilisateurs inactifs  

### ❗ Problème à résoudre :   
###     **Eviter l’exécution concurrente d’un batch Spring embarqué dans une replicaset de plusieurs pods dans un cluster Kubernetes**

### 🔍:
Ce système exécute un **traitement batch quotidien** pour détecter et **archiver les utilisateurs inactifs depuis plus de 90 jours**, à l’aide d’un `CronJob` Kubernetes.
**L'application elle-même** qui décide *quand* exécuter le batch, **mais Kubernetes** qui :

1. Planifie l'exécution à intervalles réguliers (**`schedule`**)
2. Crée un **pod éphémère** (temporaire) qui :
    - Lance l’application Spring Boot
    - Exécute le batch (via **`@PostConstruct`** ou logique d’init)
    - Termine (le pod est détruit)  
### **🧠 Avantages de cette approche :**

- Aucun risque de **concurrence** si tu as plusieurs pods ailleurs
- Pas besoin de gérer une logique de planification avec **`@Scheduled`** ou des threads
- Kubernetes gère tout : planification, isolation, redémarrage si échec, etc.
- Très propre pour les **batchs ponctuels ou planifiés**
---

### 🧾 Fonctionnalités principales

- 📆 **Planification automatique** via `CronJob` Kubernetes
- 🧼 **Archivage** des utilisateurs dans une table dédiée `archived_users`
- 🗑️ **Suppression** des utilisateurs archivés de la table `users`
- ⚙️ **Spring Batch** avec `JdbcCursorItemReader` et `CompositeItemWriter`
- 🐳 Déployable sous forme de **pod batch éphémère**

---

### 🛠️ Stack technique

| Composant | Description |
|-----------|-------------|
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="24"/> Spring Boot | Application Java |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="24"/> Spring Batch | Orchestration du traitement batch |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="24"/> PostgreSQL / H2 | Base de données relationnelle |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/kubernetes/kubernetes-plain.svg" width="24"/> Kubernetes | Orchestration des pods/applications |
| <img src="https://img.icons8.com/fluency/48/cron-job.png" width="24"/> CronJob K8s | Déclencheur automatique quotidien |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg" width="24"/> Docker | Conteneurisation de l’application batch |


## 📂 Structure du projet

Voici l’organisation des fichiers du projet `inactive-user-archiver` :

![image](https://github.com/user-attachments/assets/bb4f65a9-7cc0-4d11-a99f-7c574111abe7)
![image](https://github.com/user-attachments/assets/b6e1037b-6217-44d0-b7fc-d29a9243325a)

---  

## 🛢️ Structure de la base de données  

![image](https://github.com/user-attachments/assets/b5a158d5-db61-43c3-b101-f2891822c224)

---

## 📊 Diagramme de flux

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
