package com.sfmckenrick.assessment.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Web Security Configuration to enable basic auth and role based permissions.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Configuration method that creates the in-memory users and roles.
     * @param authMgrBuilder - The autowired AuthenticationMangerBuilder
     * @throws Exception - If an error occurs while setting users.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authMgrBuilder) throws Exception {
        // TODO: Replace this with a more robust Database solution for storing roles.
        // To save time for the assessment, in-memory was chosen.
        authMgrBuilder.inMemoryAuthentication()
                // These credentials should ideally be pulled from a secret store.
                .withUser("admin").password("{noop}password")
                    .roles("ADMIN")
                    .authorities("WRITE", "READ")
                .and()
                .withUser("user").password("{noop}password")
                    .roles("USER")
                    .authorities("READ");
    }

    /**
     * Configures BasicAuth for all v1 endpoints.
     * @param http - The HttpSecurity to configure.
     * @throws Exception - If an error occurs during configuration.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/v1/get-person/**").hasAnyAuthority("READ", "WRITE")
                .antMatchers("/v1/delete-person/**", "/v1/post-person/**").hasAuthority("WRITE")
                .anyRequest().denyAll()
                .and()
                .httpBasic();
    }
}
