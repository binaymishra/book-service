package com.book.service;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.book.service.db")
@EnableTransactionManagement
public class DataBaseConfiguration {

  /*
   * Spring Bean configurations.
   */
    @Bean(name="dataSource")
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("classpath:schema.sql")
            .addScript("classpath:data.sql")
            .build();
    }
    @Bean(name="transcationManager")
    public DataSourceTransactionManager transcationManager(){
      return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public ApplicationResource loadIndex(){
      return new ApplicationResource();
    }

}
