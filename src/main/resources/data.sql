-- Données de test pour la table users (Spring Batch va archiver ceux inactifs > 90 jours)

INSERT INTO users (username, email, last_login_date)
VALUES
    ('alice', 'alice@example.com', DATEADD('DAY', -100, CURRENT_DATE())),
    ('bob', 'bob@example.com', DATEADD('DAY', -95, CURRENT_DATE())),
    ('carla', 'carla@example.com', DATEADD('DAY', -20, CURRENT_DATE())),
    ('david', 'david@example.com', DATEADD('DAY', -200, CURRENT_DATE())),
    ('emilie', 'emilie@example.com', DATEADD('DAY', -5, CURRENT_DATE())),

    -- ✅ 3 utilisateurs supplémentaires inactifs
    ('franck', 'franck@example.com', DATEADD('DAY', -120, CURRENT_DATE())),
    ('gisèle', 'gisele@example.com', DATEADD('DAY', -365, CURRENT_DATE())),
    ('hugo', 'hugo@example.com', DATEADD('DAY', -91, CURRENT_DATE()));
