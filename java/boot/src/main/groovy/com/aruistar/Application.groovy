package com.aruistar

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

    public DispatcherServlet flexDispatcherServlet() {
        XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        appContext.setConfigLocation("classpath:/META-INF/flex-servlet.xml");
        return new DispatcherServlet(appContext);
    }

    @Bean
    public ServletRegistrationBean dispatcherRegistrationFlex() {
        ServletRegistrationBean registration = new ServletRegistrationBean(flexDispatcherServlet(), "/messagebroker/*");
        registration.name = 'flex'
        registration.setLoadOnStartup(1)
        return registration;
    }

}
