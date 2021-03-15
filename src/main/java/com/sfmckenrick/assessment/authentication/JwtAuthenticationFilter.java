package com.sfmckenrick.assessment.authentication;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.isEmpty;

/**
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * The JWT utility to use for token resolution.
     */
    private JwtUtil jwtUtil;

    /**
     * The Service uses to fetch UserDetails.
     */
    private UserDetailsService userDetailsService;

    /**
     * Constructor.
     * @param jwtUtil - The JwtUtil to set.
     * @param userDetailsService - The UserDetailsService to set.
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // Ensure that this request has a token
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Extract the username and validate the token
        final String token = header.split(" ")[1].trim();
        Optional<String> maybeUser = jwtUtil.extractUsername(token);

        // Set the credentials on the SecurityContext
        if (maybeUser.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Get user identity and set it on the spring security context
            UserDetails userDetails = userDetailsService.loadUserByUsername(maybeUser.get());

            UsernamePasswordAuthenticationToken
                    authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.getAuthorities()
            );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}