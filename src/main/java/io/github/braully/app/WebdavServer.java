package io.github.braully.app;

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
import io.github.braully.web.WebDAVServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author Braully Rocha da Silva
 */
@SpringBootApplication
@ComponentScan({"com.github.braully"})
@EnableWebSecurity
public class WebdavServer
        implements WebMvcConfigurer, ServletContextInitializer {

    public static void main(String... args) {
        SpringApplication.run(WebdavServer.class, args);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        WebDAVServlet webdavServlet = new WebDAVServlet();
        var servletRegistry = new ServletRegistrationBean(webdavServlet, "/dav/*");
        servletRegistry.setLoadOnStartup(1);
        return servletRegistry;
    }

    //https://stackoverflow.com/questions/48453980/spring-5-0-3-requestrejectedexception-the-request-was-rejected-because-the-url
    @Bean
    public HttpFirewall looseHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setUnsafeAllowAnyHttpMethod(true);
        //TODO: add all webdav methods
        //firewall.setAllowedHttpMethods(Arrays.asList("GET", "POST"));
        firewall.setAllowSemicolon(true);
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowBackSlash(true);
        firewall.setAllowUrlEncodedPercent(true);
        firewall.setAllowUrlEncodedPeriod(true);
        return firewall;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @Configuration
//    //@Order(2)
//    public static class SpringWebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//        /*
//         * https://stackoverflow.com/questions/44064346/spring-security-filter-
//         * authenticates-sucessfuly-but-sends-back-403-response
//         */
//        @Override
//        public void configure(WebSecurity web) throws Exception {
//            web.ignoring().antMatchers("/pkg/**", "/assets/**", "/*resource/**",
//                    "/favicon.ico", "/error**", "/error/**");
//        }
//
//        protected void configure(HttpSecurity http) throws Exception {
//            http.csrf().disable()
//                    .authorizeRequests()// Login urls
//                    .antMatchers("/login**", "/enter**", "/logout**").permitAll()
//                    .and().authorizeRequests().anyRequest()
//                    .authenticated().and()
//                    .formLogin().loginPage("/enter")
//                    .loginProcessingUrl("/login")
//                    .defaultSuccessUrl("/index")
//                    .permitAll().and().logout().deleteCookies("JSESSIONID")
//                    .invalidateHttpSession(true).permitAll();
//
//            // Permit iframes from same origin;
//            // https://stackoverflow.com/questions/28647136/how-to-disable-x-frame-options-response-header-in-spring-security
//            http.headers().frameOptions().sameOrigin();
//
//        }
//    }
    ///https://stackoverflow.com/questions/35890540/when-to-use-spring-securitys-antmatcher
    @Configuration
    @Order(1)
    public static class WebDavSecurityConfig extends WebSecurityConfigurerAdapter {

        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .antMatcher("/dav/**")
                    .antMatcher("/webdav/**")
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and().httpBasic();
        }
    }
}
