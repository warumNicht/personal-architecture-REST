package architecture.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties(value = ProfileConfiguration.class)
@ConfigurationProperties(prefix = "spring.datasource")
public class ProfileConfiguration {
    private String driverClassName;
    private String url;
    @Value(value = "${app.message}")
    private String message;

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Profile("dev")
    @Bean
    public String devDatabaseConnection() {
        System.out.println(this.message);
        System.out.println("DB connection for DEV - H2");
        System.out.println(this.driverClassName);
        System.out.println(this.url);
        return "DB connection for DEV - H2";
    }

    @Profile("test")
    @Bean
    public String testDatabaseConnection() {
        System.out.println(this.message);
        System.out.println("DB Connection to RDS_TEST - Low Cost Instance");
        System.out.println(this.driverClassName);
        System.out.println(this.url);
        return "DB Connection to RDS_TEST - Low Cost Instance";
    }
}
