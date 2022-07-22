/*******************************************************************************************************************************
SourceDatabaseConfiguration.java

Copyright © 2022, DroidSoft. All rights reserved.
The Programs (which include both the software and documentation) contain proprietary information of DroidSoft;
they are provided under a license agreement containing restrictions on use and disclosure and are also protected by
copyright, patent and other intellectual and industrial property law. Reverse engineering, disassembly or de-compilation of
the programs is prohibited.
Program Documentation is licensed for use solely to support the deployment of the Programs and not for any other
purpose.
The information contained in this document is subject to change without notice. If you find any problems in the
documentation, please report them to us in writing. DroidSoft does not warrant that this document is error free.
Except as may be expressly permitted in your license agreement for these Programs, no part of these Programs may be
reproduced or transmitted in any form or by any means, electronic or mechanical, for any purpose, without the express
written permission of DroidSoft.

Author : ymohammad
Date   : Jul 20, 2022

Last modified by : ymohammad
Last modified on : Jul 20, 2022

*******************************************************************************************************************************/

package in.droidsoft.dbmanager.exportdb.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
* Class SourceDatabaseConfiguration
*/
@Configuration
@PropertySources({ @PropertySource("classpath:application.properties") })
@EnableJpaRepositories(basePackages = "in.droidsoft.dbmanager.exportdb.rdbms.oracle.repository", 
						entityManagerFactoryRef = "srcEntityManager", 
						transactionManagerRef = "srcTransactionManager")
public class SourceDatabaseConfiguration {
	@Autowired
    private Environment env;

    public SourceDatabaseConfiguration() {
        super();
    }

    @Bean(name = "srcEntityManager")
    public LocalContainerEntityManagerFactoryBean appsEntityManager() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(appsDataSource());
        em.setPackagesToScan("in.droidsoft.dbmanager.exportdb.rdbms.model");
        em.setPersistenceUnitName("source_database");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        em.setJpaPropertyMap(properties);
        em.afterPropertiesSet();
        return em;
    }

    @Bean(name = "srcDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource appsDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager appsTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(appsEntityManager().getObject());
        return transactionManager;
    }
}
