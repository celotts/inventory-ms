package com.celotts.taxservice.infrastructure.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSourceLogger {

    private final DataSource dataSource;
    private final MessageSource messageSource;

    /** Traduce con fallback: si no existe la clave, devuelve la propia clave. */
    private String t(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, code, locale); // <-- fallback=code
    }

    /** Traducción ultra-segura para usar dentro de catch (no depende de MessageSource). */
    private String safe(String code, Object... args) {
        try {
            return t(code, args);
        } catch (Exception e) {
            return code; // último recurso
        }
    }

    @PostConstruct
    public void logDataSourceInfo() {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData md = conn.getMetaData();

            String sep = "──────────────────────────────";
            log.info(sep);
            log.info("{}", t("log.ds.section.db"));
            log.info(sep);

            log.info("{}: {}", t("log.ds.product"), md.getDatabaseProductName());
            log.info("{}: {}.{}", t("log.ds.version"), md.getDatabaseMajorVersion(), md.getDatabaseMinorVersion());
            log.info("{}: {} ({})", t("log.ds.driver"), md.getDriverName(), md.getDriverVersion());
            log.info("{}: {}", t("log.ds.autocommit"), conn.getAutoCommit());
            log.info("{}: {}", t("log.ds.tx"), isolationName(conn.getTransactionIsolation()));

            if (dataSource instanceof HikariDataSource hikari) {
                log.info(sep);
                log.info("{}", t("log.ds.section.hikari"));
                log.info(sep);

                log.info("{}: {}", t("log.ds.poolName"), hikari.getPoolName());
                log.info("{}: {}", t("log.ds.minIdle"), hikari.getMinimumIdle());
                log.info("{}: {}", t("log.ds.maxPool"), hikari.getMaximumPoolSize());
                log.info("{}: {} ms", t("log.ds.connTimeoutMs"), hikari.getConnectionTimeout());
            }

            log.info(sep);
        } catch (Exception e) {
            // ¡Nunca vuelvas a llamar a t(...) directo si falta el bundle!
            log.warn("{}: {}", safe("log.ds.error"), e.getMessage(), e);
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