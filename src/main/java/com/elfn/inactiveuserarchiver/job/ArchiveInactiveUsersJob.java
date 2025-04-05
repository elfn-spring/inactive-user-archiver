package com.elfn.inactiveuserarchiver.job;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Elimane
 */

/**
 * Composant responsable du lancement automatique du job Spring Batch
 * après l'initialisation du contexte Spring.
 */
@Component
public class ArchiveInactiveUsersJob {

    private final JobLauncher jobLauncher;

    private final Job archiveInactiveUsersJob;

    private static final Logger log = LoggerFactory.getLogger(ArchiveInactiveUsersJob.class);


    public ArchiveInactiveUsersJob(JobLauncher jobLauncher, Job archiveInactiveUsersJob) {
        this.jobLauncher = jobLauncher;
        this.archiveInactiveUsersJob = archiveInactiveUsersJob;
    }

    /**
     * Méthode appelée automatiquement après l'initialisation du bean Spring.
     * Elle lance le job Spring Batch avec des paramètres dynamiques (date/heure d'exécution).
     *
     * <p>
     * ⚠️ Cette méthode est exécutée à chaque démarrage de l'application.
     * À ne pas utiliser si plusieurs instances de l'application sont déployées (ex. : Kubernetes avec plusieurs pods),
     * sauf si une stratégie de synchronisation ou d'isolation du batch est en place.
     * </p>
     */
    @PostConstruct
    public void launchJob() {
        try {
            // Construction des paramètres du job (ex. : identifiant unique avec la date)
            Map<String, JobParameter<?>> parameters = new HashMap<>();
            parameters.put("launchTime", new JobParameter<>(LocalDateTime.now().toString(), String.class));
            JobParameters jobParameters = new JobParameters(parameters);

            log.info("Lancement du job d'archivage des utilisateurs inactifs...");

            // Exécution du job avec les paramètres construits
            JobExecution execution = jobLauncher.run(archiveInactiveUsersJob, jobParameters);

            log.info("Statut du job : {}", execution.getStatus());

        } catch (Exception e) {
            log.error("Erreur lors de l'exécution du batch", e);
        }
    }
}
