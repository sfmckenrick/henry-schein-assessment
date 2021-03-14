package com.sfmckenrick.assessment.personManagement;

import com.sfmckenrick.assessment.personManagement.exception.AddressNotFoundException;
import com.sfmckenrick.assessment.personManagement.exception.ClubNotFoundException;
import com.sfmckenrick.assessment.personManagement.exception.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
     * The configured repository for the ClubMembership mapping entity.
     */
    private final ClubMembershipRepository clubMembershipRepository;

    /**
     * The configured repository for the Club entity.
     */
    private final ClubRepository clubRepository;

    /**
     * The configured repository for the Person entity.
     */
    private final PersonRepository personRepository;

    /**
     * Constructor.
     * @param addressRepository - The repository to use for interacting with Address entities.
     * @param clubMembershipRepository - The repository to use for interacting with Club/Person mappings.
     * @param clubRepository - The repository to use for interacting with Club entities.
     * @param personRepository - The repository to use for interacting with Person entities.
     */
    @Autowired
    public PersonManagementService(AddressRepository addressRepository,
                                   ClubMembershipRepository clubMembershipRepository,
                                   ClubRepository clubRepository,
                                   PersonRepository personRepository) {
        this.addressRepository = addressRepository;
        this.clubMembershipRepository = clubMembershipRepository;
        this.clubRepository = clubRepository;
        this.personRepository = personRepository;
    }

    /**
     * Gets an Optional-wrapped Person object by its ID
     * @param id - The ID of the person.
     * @return A Person with matching ID.
     */
    public Person getPersonById(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
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
    public void deletePersonById(long id) {
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
    public List<Address> getAddressByPersonId(long id) {
        return addressRepository.findByPersonId(id);
    }

    /**
     * Gets an Optional-wrapped Address object by its ID.
     * @param id - The ID of the Address.
     * @return An Address with matching ID.
     */
    public Address getAddressById(long id) {
        return addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id));
    }

    /**
     * Deletes all Address attached to a specific Person.
     * @param id - The ID of the Person whose Addresses are to be deleted.
     */
    @Transactional
    public void deleteAllAddressForPersonById(long id) {
        addressRepository.deleteAddressByPersonId(id);
    }

    /**
     * Deletes an Address by its ID.
     * @param id - The ID of the Address to delete.
     */
    @Transactional
    public void deleteAddressById(long id) {
        addressRepository.deleteById(id);
    }

    /**
     * Saves (Inserts or Updates) a Club.
     * @param club - The Club object to save.
     * @return The saved Club.
     */
    @Transactional
    public Club saveClub(Club club) {
        return clubRepository.save(club);
    }

    /**
     * Deletes a Club by its name identifier.
     * @param name The name of the Club to delete.
     */
    @Transactional
    public void deleteClubByName(String name) {
        clubRepository.deleteById(name);
    }

    /**
     * Gets a Club by its name identifier.
     * @param name The name of the club to get.
     * @return A Club object with a matching name.
     */
    public Club getClubByName(String name) {
        return clubRepository.findById(name).orElseThrow(() ->new ClubNotFoundException(name));
    }

    /**
     * Creates a new relationship between a Person and a Club.
     * @param personId - The person to add to a Club.
     * @param clubName - The Club where the person is to be added.
     * @return The result ClubMembership.
     */
    public ClubMembership addClubMembership(long personId, String clubName) {
        Person person = getPersonById(personId);
        Club club = getClubByName(clubName);
        ClubMembership clubMembership = new ClubMembership(person, club);
        return clubMembershipRepository.save(clubMembership);
    }

    /**
     * Deletes a specific relationship between a Person and a Club.
     * @param personId - The ID of the Person who belongs to a club.
     * @param clubName - The Name of the Club where the person is a member.
     */
    @Transactional
    public void deleteClubMembership(long personId, String clubName) {
        clubMembershipRepository.deleteByIdPersonIdAndIdClubName(personId, clubName);
    }

    /**
     * Checks to see if a Person has membership with a Club.
     * @param personId - The ID of the Person to check.
     * @param clubName - The name of the Club to check.
     * @return True if member, false if no relation.
     */
    public boolean isPersonClubMember(long personId, String clubName) {
        return clubMembershipRepository.existsByIdPersonIdAndIdClubName(personId, clubName);
    }

    /**
     * Deletes all Club memberships involving a single Person.
     * @param id - The ID of the Person to remove.
     */
    @Transactional
    public void deleteClubMembershipByPerson(long id) {
        clubMembershipRepository.deleteByIdPersonId(id);
    }


    /**
     * Deletes all Club memberships involving a single Club.
     * @param name - The name of the Club to remove.
     */
    @Transactional
    public void deleteClubMembershipByClub(String name) {
        clubMembershipRepository.deleteByIdClubName(name);
    }

    /**
     * Gets all Clubs where a Person is a member.
     * @param id - The ID of the Person to Check.
     * @return A list of Clubs where the supplied Person is a member.
     */
    public List<Club> getClubMembershipForPerson(Long id) {
        return clubMembershipRepository.findByIdPersonId(id)
                .stream()
                .map(ClubMembership::getClub)
                .collect(Collectors.toList());
    }

    /**
     * Gets all Persons that belong to a specified Club.
     * @param name - The name of the Club to check.
     * @return A list of Persons where they belong to a supplied Club.
     */
    public List<Person> getPersonMembersForClub(String name) {
        return clubMembershipRepository.findByIdClubName(name)
                .stream()
                .map(ClubMembership::getPerson)
                .collect(Collectors.toList());
    }

    /**
     * Clears the entire datastore.
     */
    protected void clear() {
        personRepository.deleteAll();
        clubRepository.deleteAll();
    }
}
