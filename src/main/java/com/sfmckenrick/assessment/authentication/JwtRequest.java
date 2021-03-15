package com.sfmckenrick.assessment.authentication;

import java.io.Serializable;

/**
 * Request object that contains a username and password field. This is used for generating the JWT token.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
public class JwtRequest implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = 6952662722745741497L;

    /**
     * The username of the request.
     */
    private String username;

    /**
     * The password of the request.
     */
    private String password;

    /**
     * Serialization Constructor.
     */
    public JwtRequest()
    {

    }

    public JwtRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
