package com.github.braully.app;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.github.braully.web.WebdavServletDecorator;
import com.github.braully.web.WebdavServletWrapp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author Braully Rocha da Silva
 */
@SpringBootApplication
@ComponentScan({"com.github.braully"})
public class WebdavServer
        implements WebMvcConfigurer {

    public static void main(String... args) {
        SpringApplication.run(WebdavServer.class, args);
    }

//    @Override
//    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//    }
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        WebdavServletDecorator webdavServlet = new WebdavServletDecorator();
        var servletRegistry = new ServletRegistrationBean(webdavServlet, "/webdav/*");
        servletRegistry.setLoadOnStartup(1);
        return servletRegistry;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean2() {
        var webdavServlet = new WebdavServletWrapp();
        var servletRegistry = new ServletRegistrationBean(webdavServlet, "/webdav2/*");
        servletRegistry.setLoadOnStartup(1);
        return servletRegistry;
    }
}
