package com.example.ordermanagement.configuration;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Random;

@Configuration
@ConditionalOnClass({MariaDB4jSpringService.class, DataSource.class})
@EnableConfigurationProperties(value = {MariaDB4jProperties.class})
public class MariaDB4jAutoConfiguration {

    private final MariaDB4jProperties mariaDB4jProperties;

    public MariaDB4jAutoConfiguration(MariaDB4jProperties mariaDB4jProperties) {
        this.mariaDB4jProperties = mariaDB4jProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public MariaDB4jSpringService mariaDB4jSpringService() {
        MariaDB4jSpringService mariaDB4jSpringService = new MariaDB4jSpringService();
        DBConfigurationBuilder dBConfigurationBuilder = mariaDB4jSpringService.getConfiguration();
        dBConfigurationBuilder.addArg("--character-set-server=utf8");
        dBConfigurationBuilder.addArg("--lower_case_table_names=1");
        dBConfigurationBuilder.addArg("--collation-server=utf8_general_ci");
        dBConfigurationBuilder.addArg("--user=root");

        String dataDir = StringUtils.isEmpty(mariaDB4jProperties.getDataDir()) ? dBConfigurationBuilder.getDataDir() : mariaDB4jProperties.getDataDir();
        String libDir = StringUtils.isEmpty(mariaDB4jProperties.getLibDir()) ? dBConfigurationBuilder.getLibDir() : mariaDB4jProperties.getLibDir();
        String socket = StringUtils.isEmpty(mariaDB4jProperties.getSocket()) ? dBConfigurationBuilder.getSocket() : mariaDB4jProperties.getSocket();
        String baseDir = StringUtils.isEmpty(mariaDB4jProperties.getBaseDir()) ? dBConfigurationBuilder.getBaseDir() : mariaDB4jProperties.getBaseDir();

        mariaDB4jSpringService.setDefaultBaseDir(baseDir);
        mariaDB4jSpringService.setDefaultDataDir(dataDir);
        mariaDB4jSpringService.setDefaultLibDir(libDir);
        mariaDB4jSpringService.setDefaultPort(new Random().nextInt(1000) + 60000);
        mariaDB4jSpringService.setDefaultSocket(socket);
        return mariaDB4jSpringService;
    }

    @Bean
    @DependsOn("mariaDB4jSpringService")
    @Primary
    public DataSource dataSource(DataSourceProperties dataSourceProperties, MariaDB4jSpringService mariaDB4jSpringService) throws ManagedProcessException {
        String databaseName = mariaDB4jProperties.getDatabaseName();
        mariaDB4jSpringService.getDB().createDB(databaseName);
        DBConfigurationBuilder dBConfiguration = mariaDB4jSpringService().getConfiguration();

        DataSourceBuilder
                .create()
                .url(dBConfiguration.getURL(databaseName))
                .driverClassName(dataSourceProperties.determineDriverClassName())
                .username(dataSourceProperties.determineUsername())
                .password(dataSourceProperties.determinePassword())
                .build();

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(dataSourceProperties.determineDriverClassName());
        config.setJdbcUrl(dBConfiguration.getURL(databaseName));
        config.setUsername(dataSourceProperties.determineUsername());
        config.setPassword(dataSourceProperties.determinePassword());
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(1);
        config.setIdleTimeout(60000L);
        config.setConnectionTestQuery("select 1");

        return new HikariDataSource(config);
    }
}
