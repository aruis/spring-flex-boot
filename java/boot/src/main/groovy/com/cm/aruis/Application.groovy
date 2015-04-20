package com.cm.aruis

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.embedded.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.web.context.support.XmlWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet

/**
 * Created by liurui on 15/4/16.
 */
@SpringBootApplication
class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args)
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        appContext.setConfigLocation("classpath:/META-INF/application-config.xml");
        return new DispatcherServlet(appContext);
    }

    @Bean
    public ServletRegistrationBean dispatcherRegistrationFlex() {
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet(), "/messagebroker/*");
        registration.addUrlMappings("/messagebroker/*")
        registration.setLoadOnStartup(1)
        return registration;
    }
}
