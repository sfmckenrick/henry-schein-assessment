package com.sfmckenrick.assessment.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Web Security Configuration to enable basic auth and role based permissions.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * The Jwt Token utility.
     */
    private final JwtUtil jwtUtil;

    /**
     * Configuration method that creates the in-memory users and roles.
     * @param jwtUtil - The jwtUtil to initialize.
     * @throws Exception - If an error occurs while setting users.
     */
    @Autowired
    public SecurityConfiguration(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        // TODO: Replace this with a more robust Database solution for storing roles.
        // To save time for the assessment, in-memory was chosen.

        //User Role
        UserDetails adminUser = User.withUsername("admin")
                .password("{noop}password")
                .roles(Role.ADMIN)
                .authorities(Authority.READ, Authority.WRITE)
                .build();

        //Manager Role
        UserDetails publicUser = User.withUsername("public")
                .password("{noop}password")
                .roles(Role.USER)
                .authorities(Authority.READ)
                .build();


        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

        userDetailsManager.createUser(adminUser);
        userDetailsManager.createUser(publicUser);

        return userDetailsManager;
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
                    .antMatchers("/v1/get-person/**").hasAnyAuthority(Authority.READ, Authority.WRITE)
                    .antMatchers("/v1/delete-person/**", "/v1/post-person/**").hasAuthority(Authority.WRITE)
                    .antMatchers("/v1/auth/token/**").authenticated()
                    .anyRequest().denyAll()
                .and()
                .httpBasic();
    }
}
