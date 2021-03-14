package com.sfmckenrick.assessment.personManagement;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Interface that handles the CRUD operations on the data store.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
public interface ClubRepository extends JpaRepository<Club, String> {
}
