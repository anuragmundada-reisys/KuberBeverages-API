package com.kuber.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.requiresChannel()
                .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
                .requiresSecure()
                .and()
                .headers()
                .frameOptions()
                .sameOrigin().addHeaderWriter(((request, response) -> {
                    response.setHeader("Cache-Control","no-cache, no-store, max-age=0, must-revalidate, private");
                    response.setHeader("Pragma","no-cache");
                    response.setHeader("Access-Control-Allow-Origin","*");
                    response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, X-Forwarded-Proto, Content-Type, Accept, Cache-Control, Pragma, Expires, X-Auth-Token");
                }));
    }
}
