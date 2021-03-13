package com.sfmckenrick.assessment.personManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Contains all business logic for persisting and retrieving all entities related to a Person.
 *
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
@Service
public class PersonManagementService {

    /**
     * The configured repository for the Address entity.
     */
    private final AddressRepository addressRepository;

    /**
     * The configured repository for the Club entity.
     */
//    private final ClubRepository clubRepository;

    /**
     * The configured repository for the Person entity.
     */
    private final PersonRepository personRepository;

    /**
     * Constructor.
     * @param addressRepository - The repository to use for interacting with Address entities.
//     * @param clubRepository - The repository to use for interacting with Club entities.
     * @param personRepository - The repository to use for interacting with Person entities.
     */
    @Autowired
    public PersonManagementService(AddressRepository addressRepository,
                                   /*ClubRepository clubRepository,*/
                                   PersonRepository personRepository) {
        this.addressRepository = addressRepository;
//        this.clubRepository = clubRepository;
        this.personRepository = personRepository;
    }

    /**
     * Gets an Optional-wrapped Person object by its ID
     * @param id - The ID of the person.
     * @return An Optional Person with matching ID.
     */
    public Optional<Person> getPersonById(Long id) {
        return personRepository.findById(id);
    }

    /**
     * Saves (Inserts or Updates) a Person.
     * @param person - The Person object to save.
     * @return The saved Person.
     */
    @Transactional
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    /**
     * Deletes a Person by its ID.
     * @param id - The ID of the Person to delete.
     */
    @Transactional
    public void deletePersonById(Long id) {
        personRepository.deleteById(id);
    }

    /**
     * Saves (Inserts or Updates) an Address.
     * @param address - The Address object to save.
     * @return The saved Address.
     */
    @Transactional
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    /**
     * Gets the list of all Address objects by its attached Person's ID
     * @param id - The ID of the Person to find all Addresses.
     * @return The List of Address objects.
     */
    public List<Address> getAddressByPersonId(Long id) {
        return addressRepository.findByPersonId(id);
    }

    /**
     * Gets an Optional-wrapped Address object by its ID.
     * @param id - The ID of the Address.
     * @return An Optional Address with matching ID.
     */
    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }

    /**
     * Deletes all Address attached to a specific Person.
     * @param id - The ID of the Person whose Addresses are to be deleted.
     */
    @Transactional
    public void deleteAllAddressForPersonById(Long id) {
        addressRepository.deleteAddressByPersonId(id);
    }

    /**
     * Deletes an Address by its ID.
     * @param id - The ID of the Address to delete.
     */
    public void deleteAddressById(Long id) {
        addressRepository.deleteById(id);
    }

}
