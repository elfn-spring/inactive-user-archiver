package com.elfn.inactiveuserarchiver.config;

import com.elfn.inactiveuserarchiver.model.Users;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


import javax.sql.DataSource;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

/**
 * Configuration principale du traitement batch Spring.
 *
 * Cette classe définit :
 * - Le reader qui lit les utilisateurs inactifs depuis la table `users`
 * - Deux writers (archivage + suppression)
 * - Un writer composite qui enchaîne les deux actions
 * - Un step unique pour l’archivage des utilisateurs
 * - Un job complet orchestrant ce step
 *
 * @author Elimane
 */

@Configuration
@EnableBatchProcessing
public class BatchConfig {


    @Autowired
    private DataSource dataSource;


    /**
     * Reader qui sélectionne tous les utilisateurs inactifs depuis plus de 90 jours.
     *
     * @return un JdbcCursorItemReader configuré
     */
    @Bean
    public JdbcCursorItemReader<Users> reader() {
        JdbcCursorItemReader<Users> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT * FROM users WHERE last_login_date < ?");
        reader.setPreparedStatementSetter(ps -> ps.setDate(1, java.sql.Date.valueOf(LocalDate.now().minusDays(90))));
        reader.setRowMapper((ResultSet rs, int rowNum) -> new Users(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getDate("last_login_date").toLocalDate()
        ));
        return reader;
    }


    /**
     * Writer responsable de l’insertion des utilisateurs dans la table `archived_users`.
     *
     * @return un ItemWriter configuré pour l’archivage
     */
    @Bean
    public ItemWriter<Users> archiveWriter() {
        return items -> {
            JdbcBatchItemWriter<Users> writer = new JdbcBatchItemWriter<>();
            writer.setDataSource(dataSource);
            writer.setSql("INSERT INTO archived_users (id, username, email, archived_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)");
            writer.setItemPreparedStatementSetter((user, ps) -> {
                ps.setLong(1, user.getId());
                ps.setString(2, user.getUsername());
                ps.setString(3, user.getEmail());
            });
            writer.afterPropertiesSet();
            writer.write(items);
        };
    }


    /**
     * Writer responsable de la suppression des utilisateurs de la table `users`.
     *
     * @return un ItemWriter configuré pour la suppression
     */
    @Bean
    public ItemWriter<Users> deleteWriter() {
        return items -> {
            JdbcBatchItemWriter<Users> writer = new JdbcBatchItemWriter<>();
            writer.setDataSource(dataSource);
            writer.setSql("DELETE FROM users WHERE id = ?");
            writer.setItemPreparedStatementSetter((user, ps) -> ps.setLong(1, user.getId()));
            writer.afterPropertiesSet();
            writer.write(items);
        };
    }


    /**
     * Writer composite qui combine l’archivage puis la suppression.
     *
     * @return un CompositeItemWriter qui exécute les deux actions en chaîne
     */
    @Bean
    public ItemWriter<Users> compositeWriter() {
        CompositeItemWriter<Users> writer = new CompositeItemWriter<>();
        writer.setDelegates(List.of(archiveWriter(), deleteWriter()));
        return writer;
    }


    /**
     * Step qui exécute le processus de lecture, archivage et suppression des utilisateurs inactifs.
     *
     * @param jobRepository le repository Spring Batch
     * @param transactionManager gestionnaire de transactions
     * @return le Step d'archivage
     */
    @Bean
    public Step archiveStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("archiveStep", jobRepository)
                .<Users, Users>chunk(10, transactionManager)
                .reader(reader())
                .writer(compositeWriter())
                .build();
    }


    /**
     * Job principal qui orchestre le step d’archivage.
     *
     * @param jobRepository le repository Spring Batch
     * @param archiveStep le step d’archivage à exécuter
     * @return le Job complet prêt à être lancé
     */
    @Bean
    public Job archiveInactiveUsersJob(JobRepository jobRepository, Step archiveStep) {
        return new JobBuilder("archiveInactiveUsersJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(archiveStep)
                .build();
    }


}
