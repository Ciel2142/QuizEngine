package com.example.demo.utilitis.security;


import com.example.demo.utilitis.repositories.UserRepository;
import com.example.demo.utilitis.users.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPoint authEntryPoint;


    @Autowired
    private MyUserDetailsService myUserDetailsService;


    @Autowired
    DataSource ds;

    @Autowired
    UserRepository ur;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] allowedResources = {
                "/h2-console/**",
                "/api/register",
                "/actuator/shutdown"};

        // All requests send to the Web Server request must be authenticated

        http.authorizeRequests().
                antMatchers(allowedResources).
                permitAll().
                anyRequest().
                authenticated().
                and().
                formLogin().
                and().
                httpBasic().
                authenticationEntryPoint(authEntryPoint);

        http.csrf().disable();
        // For the H2 Console
        http.headers().frameOptions().disable();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }
}