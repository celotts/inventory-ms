package com.celotts.purchaseservice.infrastructure.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class DataSourceLogger {

    private static final Logger log = LoggerFactory.getLogger(DataSourceLogger.class);

    private final DataSource dataSource;
    private final MessageSource messageSource;

    private String t(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale(); // en @PostConstruct usará el default (p.ej., es_MX)
        return messageSource.getMessage(code, args, locale);
    }

    @PostConstruct
    public void logDataSourceInfo() {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData md = conn.getMetaData();

            String sep = "──────────────────────────────";
            log.info(sep);
            log.info("{}", t("log.ds.section.db"));     // 📦 Database Connection Details / 📦 Detalles de Conexión a BD
            log.info(sep);

            log.info("{}: {}", t("log.ds.product"), md.getDatabaseProductName());
            log.info("{}: {}.{}", t("log.ds.version"), md.getDatabaseMajorVersion(), md.getDatabaseMinorVersion());
            log.info("{}: {} ({})", t("log.ds.driver"), md.getDriverName(), md.getDriverVersion());
            log.info("{}: {}", t("log.ds.autocommit"), conn.getAutoCommit());
            log.info("{}: {}", t("log.ds.tx"), isolationName(conn.getTransactionIsolation()));

            if (dataSource instanceof HikariDataSource hikari) {
                log.info(sep);
                log.info("{}", t("log.ds.section.hikari")); // 💧 HikariCP Configuration / 💧 Configuración HikariCP
                log.info(sep);

                log.info("{}: {}", t("log.ds.poolName"), hikari.getPoolName());
                log.info("{}: {}", t("log.ds.minIdle"), hikari.getMinimumIdle());
                log.info("{}: {}", t("log.ds.maxPool"), hikari.getMaximumPoolSize());
                log.info("{}: {} ms", t("log.ds.connTimeoutMs"), hikari.getConnectionTimeout());
            }

            log.info(sep);

        } catch (Exception e) {
            log.warn("{}: {}", t("log.ds.error"), e.getMessage(), e);
        }
    }

    private String isolationName(int level) {
        return switch (level) {
            case Connection.TRANSACTION_NONE -> "NONE";
            case Connection.TRANSACTION_READ_UNCOMMITTED -> "READ_UNCOMMITTED";
            case Connection.TRANSACTION_READ_COMMITTED -> "READ_COMMITTED";
            case Connection.TRANSACTION_REPEATABLE_READ -> "REPEATABLE_READ";
            case Connection.TRANSACTION_SERIALIZABLE -> "SERIALIZABLE";
            default -> "UNKNOWN (" + level + ")";
        };
    }
}