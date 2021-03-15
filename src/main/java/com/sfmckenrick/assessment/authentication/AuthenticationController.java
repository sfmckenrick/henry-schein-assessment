package com.sfmckenrick.assessment.authentication;

import com.sfmckenrick.assessment.authentication.exception.RoleAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

/**
 * Authentication Controllor for issuing JWT.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
@RestController
@RequestMapping("v1/auth")
public class AuthenticationController {

    /**
     * The Jwt Token utility.
     */
    private JwtUtil jwtUtil;

    /**
     * The service to use to fetch UserDetails
     */
    private UserDetailsService userDetailsService;

    /**
     * The AuthenticationManager to authenticate a request.
     */
    private AuthenticationManager authenticationManager;

    /**
     * Constructor that handles dependency injection.
     * @param jwtUtil - The JwtUtil to initialize.
     * @param userDetailsService - The UserDetailsService to initialize.
     * @param authenticationManager - The AuthenticationManager to initialize.
     */
    @Autowired
    public void AuthenticationController(JwtUtil jwtUtil,
                                         UserDetailsService userDetailsService,
                                         AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "token")
    public JwtResponse createAuthenticationToken(@RequestBody JwtRequest jwtRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword());
        authenticationManager.authenticate(authentication);
        UserDetails user = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        if(!user.getAuthorities().contains(new SimpleGrantedAuthority(Role.USER))) {
            throw new RoleAccessException(Role.USER);
        }
        String token =  jwtUtil.generateToken(user);
        return new JwtResponse(token);
    }

    /**
     * Exception handling an disabled account.
     * @param e - The Exception object.
     * @param request - The request.
     * @return The Constructed response.
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Object> handleNotFound(DisabledException e, WebRequest request) {
        return new ResponseEntity<>("Account Disabled.", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Exception handling an invalid role.
     * @param e - The Exception object.
     * @param request - The request.
     * @return The Constructed response.
     */
    @ExceptionHandler(RoleAccessException.class)
    public ResponseEntity<Object> handleNotFound(RoleAccessException e, WebRequest request) {
        return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Returns a message to the user when the credentials are incorrect.
     * @param e - The Exception object.
     * @param request - The request.
     * @return The Constructed response.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleNotFound(BadCredentialsException e, WebRequest request) {
        return new ResponseEntity<>("Invalid credentials.", HttpStatus.UNAUTHORIZED);
    }


}
