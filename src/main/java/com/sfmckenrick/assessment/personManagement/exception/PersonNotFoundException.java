package com.sfmckenrick.assessment.personManagement.exception;

/**
 * Person implementation of the EntityNotFoundException
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
public class PersonNotFoundException extends EntityNotFoundException {

    /**
     * The Person Entity Type.
     */
    private static final String TYPE = "PERSON";

    /**
     * The Serialization UID.
     */
    private static final long serialVersionUID = -8370299931964887286L;

    /**
     * Constructor.
     *
     * @param identifier - The identifier of the entity.
     */
    public PersonNotFoundException(Long identifier) {
        super(TYPE, identifier);
    }
}
