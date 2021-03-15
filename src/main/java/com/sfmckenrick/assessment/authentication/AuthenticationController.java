package com.sfmckenrick.assessment.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Constructor that handles dependency injection.
     * @param jwtUtil - The jwtUtil to initialize.
     */
    @Autowired
    public void AuthenticationController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "token")
    public JwtResponse createAuthenticationToken(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
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
