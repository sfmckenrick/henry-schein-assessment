package com.sfmckenrick.assessment.personManagement;

import com.sfmckenrick.assessment.personManagement.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

/**
 * RESTful controller that facilitates the modification of the Person data objects.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
@RestController
@RequestMapping("v1")
public class PersonManagementController {

    /**
     * The Service Layer for Person Management.
     */
    private PersonManagementService service;

    @Autowired
    public PersonManagementController(PersonManagementService service) {
        this.service = service;
    }

    /**
     * Get endpoint that gets a Person by their ID.
     * @param personId - The ID of the person to get.
     * @return The retrieved Person object.
     */
    @GetMapping("get-person/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public Person getPersonById(@PathVariable Long personId) {
        return service.getPersonById(personId);
    }

    /**
     * Delete endpoint to delete a Person by their ID.
     * @param personId - The ID of the person to delete.
     */
    @DeleteMapping("delete-person/{personId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deletePersonById(@PathVariable Long personId) {
        service.deletePersonById(personId);
    }

    /**
     * Posts a Person Object to be saved (or updated) in the data layer.
     * @param person - The Person to save.
     * @return The updated Person object.
     */
    @PostMapping("post-person")
    @ResponseStatus(HttpStatus.CREATED)
    public Person insertPerson(@RequestBody Person person) {
        return service.savePerson(person);
    }

    /**
     * Exception handler that handles DataIntegrityViolationException exceptions that are thrown.
     * This indicates that either the data was malformed or that there was a constrain violation.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleIntegrityViolation(DataIntegrityViolationException e, WebRequest request) {
        String message = "Data integrity violation. Please check that the data is valid and not malformed.";
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler that handles all implementations of EntityNotFoundException exceptions.
     * @param e - The Exception object.
     * @param request - The request.
     * @return The Constructed response.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EntityNotFoundException e, WebRequest request) {
        return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }
}
