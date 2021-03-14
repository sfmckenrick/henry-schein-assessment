package com.sfmckenrick.assessment.personManagement.exception;

/**
 * Address implementation of the EntityNotFoundException
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
public class AddressNotFoundException extends EntityNotFoundException {

    /**
     * The Address Entity Type.
     */
    private static final String TYPE = "ADDRESS";

    /**
     * The Serialization UID.
     */
    private static final long serialVersionUID = -8506149916971044654L;

    /**
     * Constructor.
     *
     * @param identifier - The identifier of the entity.
     */
    public AddressNotFoundException(Long identifier) {
        super(TYPE, identifier);
    }

}
