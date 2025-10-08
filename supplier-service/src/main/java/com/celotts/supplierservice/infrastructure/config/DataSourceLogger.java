package com.celotts.supplierservice.infrastructure.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSourceLogger {
    private static final Logger log = LoggerFactory.getLogger(DataSourceLogger.class);
    private final DataSource dataSource;

    @PostConstruct
    public void LogDaSourceInfo() {
        try (Connection c = dataSource.getConnection()) {

            DatabaseMetaData md = c.getMetaData();
            log.info("DB Supplier Name: {}", md.getDatabaseProductName());
            log.info("DB Version: {}.{}", md.getDatabaseMajorVersion(), md.getDatabaseMinorVersion());
            log.info("Driver Name: {}", md.getDriverName());
            log.info("Driver Version: {}", md.getDriverVersion());
            log.info("AutoCommit: {}", md.getDriverVersion());
            log.info("Tx Isolation: {}", isolationName(c.getTransactionIsolation()));


            if(dataSource instanceof  HikariDataSource hikari) {
                log.info("Hikari pool: {}", hikari.getPoolName());
                log.info("Min Idle: {}", hikari.getMinimumIdle());
                log.info("Max Pool Size: {}", hikari.getMaximumPoolSize());
            }

        } catch (Exception e) {
            log.warn("Could not log datasource infor", e);
        }
    }

    private String isolationName(int level){
        return switch(level) {
            case Connection.TRANSACTION_NONE -> "NONE";
            case Connection.TRANSACTION_READ_UNCOMMITTED -> "READ_UNCOMMITTED";
            case Connection.TRANSACTION_READ_COMMITTED -> "READ_COMMITED";
            case Connection.TRANSACTION_REPEATABLE_READ -> "REPEATABLE_READ";
            case Connection.TRANSACTION_SERIALIZABLE -> "SERIALIZABLE";
            default -> "UNKNOW("+level+")";
        };
    }
}
