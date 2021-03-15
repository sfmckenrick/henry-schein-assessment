package com.sfmckenrick.assessment.authentication.exception;

/**
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
public class RoleAccessException extends RuntimeException{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -6825672941897140994L;

    /**
     * Constructor.
     * @param role - The invalid role.
     */
    public RoleAccessException(String role) {
        super("Invalid Role supplied. Role: " + role + " required!");
    }

}
