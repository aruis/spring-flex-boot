package com.cm.aruis

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.embedded.ServletRegistrationBean
import org.springframework.boot.context.web.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.web.context.support.XmlWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet

/**
 * Created by liurui on 15/4/16.
 */
@SpringBootApplication
class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args)
        //SpringApplication.run("classpath:/META-INF/application-config.xml", args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        appContext.setConfigLocation("classpath:/META-INF/application-config.xml");
        return new DispatcherServlet(appContext);
    }

    @Bean
    public ServletRegistrationBean dispatcherRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet(), "/messagebroker/*");

        //registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);

        registration.setLoadOnStartup(1)
        println registration.getUrlMappings()
        return registration;
//        ServletRegistrationBean registration = new ServletRegistrationBean(
//                dispatcherServlet);
//        println registration.getUrlMappings()
//        //registration.setLoadOnStartup(1)
//        registration.urlMappings = ["/messagebroker/*"]
//        println registration.getUrlMappings()
//        return registration;
    }

}
