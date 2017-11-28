package io.coinshell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@SpringBootApplication
@ServletComponentScan
@EnableAutoConfiguration
public class CoinShellServer {

    private final Logger logger = LoggerFactory.getLogger(CoinShellServer.class);

    @Autowired
    private Environment env;




    @EventListener(ContextRefreshedEvent.class)
    public void startupInfo() {

        String logFile = env.getProperty("logging.file");
        logger.debug("Logging to file " + logFile);

    }


    // TODO Add db schema and data setup - See https://github.com/mkyong/spring-embedded-database
    // Or, use FlyAway

    @Bean
    public WebMvcConfigurer corsConfigurer() {



        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                // Enable CCORS support by defining allowed methods here, and adding a custom filter
                // to WebSecurityConfig

                // See https://spring.io/blog/2015/06/08/cors-support-in-spring-framework
                //  and
                // https://stackoverflow.com/questions/40418441/spring-security-cors-filter
                logger.debug("Adding allowed methods for CORS configuration");
                registry.addMapping("/**").allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS");
            }
        };
    }



}
