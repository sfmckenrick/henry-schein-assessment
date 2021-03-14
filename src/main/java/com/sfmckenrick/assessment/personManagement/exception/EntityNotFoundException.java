package com.sfmckenrick.assessment.personManagement.exception;

/**
 * Unchecked exception that indicates that an Entity does not exist in the data store.
 * This exception will consumed by a ExceptionHandler within the PersonManagementController
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
public abstract class EntityNotFoundException extends RuntimeException {

    /**
     * Constructor.
     * @param type - The type of entity that is not able to be found.
     * @param identifier - The identifier of the entity.
     */
    public EntityNotFoundException(String type, Object identifier) {
        super("Unable to Locate Entity. Type: " + type + "; Identifier: " + identifier);
    }
}
