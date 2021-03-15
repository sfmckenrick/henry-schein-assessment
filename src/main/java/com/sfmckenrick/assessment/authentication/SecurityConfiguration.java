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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Web Security Configuration to enable basic auth and role based permissions.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtUtil jwtUtil;

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
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        // TODO: Replace this with a more robust Database solution for storing roles.
        // To save time for the assessment, in-memory was chosen.

        //User Role
        UserDetails adminUser = User.withUsername("admin")
                .password("password")
                .authorities(Role.ADMIN, Authority.READ, Authority.WRITE)
                .build();

        //Manager Role
        UserDetails publicUser = User.withUsername("public")
                .password("password")
                .authorities(Role.USER, Authority.READ)
                .build();


        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

        userDetailsManager.createUser(adminUser);
        userDetailsManager.createUser(publicUser);

        return userDetailsManager;
    }

    @Bean
    public RestrictedBasicAuthenticationFilter restrictedBasicAuthenticationFilter() throws Exception {
        return new RestrictedBasicAuthenticationFilter(authenticationManagerBean(),
                userDetailsService(),
                Role.ADMIN);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService());
    }

    /**
     * Configures BasicAuth for all v1 endpoints.
     * @param http - The HttpSecurity to configure.
     * @throws Exception - If an error occurs during configuration.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Configure Route access
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/v1/get-person/**").hasAnyAuthority(Authority.READ, Authority.WRITE)
                    .antMatchers("/v1/delete-person/**", "/v1/post-person/**").hasAuthority(Authority.WRITE)
                    .antMatchers("/v1/auth/token/**").permitAll()
                    .anyRequest().denyAll();

        // Add filters ADMIN - Basic Auth, User - JWT.
        http
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilter(restrictedBasicAuthenticationFilter());
    }
}
