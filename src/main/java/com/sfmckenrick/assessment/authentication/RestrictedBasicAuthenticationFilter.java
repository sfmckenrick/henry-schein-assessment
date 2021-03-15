package com.sfmckenrick.assessment.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
public class RestrictedBasicAuthenticationFilter extends BasicAuthenticationFilter {

    /**
     * The whitelist of allowed roles.
     */
    private final Set<GrantedAuthority> whitelist = new HashSet<>();

    /**
     * The Auth Converter to convert request into The user/pass token.
     */
    private BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();

    /**
     * Service that contains the UserDetails.
     */
    private UserDetailsService userDetailsService;

    /**
     * Constructor that initialize the super class, whitelist and User service.
     * @param authenticationManager - The Authentication manager.
     * @param userDetailsService - The UserDetailsService
     * @param role - Vararg of roles to add to the whitelist.
     */
    public RestrictedBasicAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, String ... role) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.whitelist.clear();
        List<GrantedAuthority> roles = Arrays.stream(role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        this.whitelist.addAll(roles);

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authRequest = this.authenticationConverter.convert(request);

        if (authRequest != null && validateRole(authRequest.getName())) {
            super.doFilterInternal(request, response, chain);
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * Checks the user's role against the whitelist.
     * @param username - The username of the requesting user.
     * @return True if in list of role whitelist.
     */
    private boolean validateRole(String username) {

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return userDetails.getAuthorities().stream().anyMatch(whitelist::contains);
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }
}
