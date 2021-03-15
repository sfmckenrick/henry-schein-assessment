package com.sfmckenrick.assessment.personManagement.exception;

/**
 * Club implementation of the EntityNotFoundException
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
public class ClubNotFoundException extends EntityNotFoundException {

    /**
     * The Club Entity Type.
     */
    private static final String TYPE = "CLUB";

    /**
     * The Serialization UID.
     */
    private static final long serialVersionUID = 8419100561485406377L;

    /**
     * Constructor.
     *
     * @param identifier - The identifier of the entity.
     */
    public ClubNotFoundException(String identifier) {
        super(TYPE, identifier);
    }

}
