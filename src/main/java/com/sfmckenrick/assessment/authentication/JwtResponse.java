package com.sfmckenrick.assessment.authentication;

import java.io.Serializable;

/**
 * Response that will contain a JWT TOken
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
public class JwtResponse implements Serializable {
    /**
     * Serialization ID
     */
    private static final long serialVersionUID = 1978852629158804905L;

    /**
     * The token.
     */
    private String token;

    public JwtResponse() {
    }

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
