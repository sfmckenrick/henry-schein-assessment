package com.sfmckenrick.assessment.personManagement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Interface that handles the CRUD operations on the data store.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Finds all Addresses who have a common Person.
     * @param personId - The id of the Person to search.
     * @return The list of Addresses.
     */
    List<Address> findByPersonId(Long personId);

    /**
     * Deletes all Addresses who have a common Person.
     * @param personId - The id of the Person to search.
     */
    void deleteAddressByPersonId(Long personId);
}
