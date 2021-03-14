package com.sfmckenrick.assessment.personManagement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Interface that handles the CRUD operations on the data store.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
public interface ClubMembershipRepository extends JpaRepository<ClubMembership, ClubMembershipKey> {

    /**
     * Finds all Clubs that a Person has membership.
     * @param personId - The id of the Person to search.
     * @return The list of ClubMembership.
     */
    List<ClubMembership> findByIdPersonId(Long personId);

    /**
     * Finds all Persons who have membership to a specific club.
     * @param clubName - The name of the Club to search.
     * @return The list of ClubMembership.
     */
    List<ClubMembership> findByIdClubName(String clubName);

    /**
     * Deletes all ClubMemberships belonging to a Person.
     * @param personId - The Person to remove.
     */
    void deleteByIdPersonId(Long personId);

    /**
     * Deletes all ClubMemberships belonging to a Club.
     * @param clubName - The Club to remove.
     */
    void deleteByIdClubName(String clubName);

    /**
     * Checks if the specified relationship exists.
     * @param personId - The Person to check.
     * @param clubName - The Club to check.
     * @return True if a relationship exists, false if not.
     */
    boolean existsByIdPersonIdAndIdClubName(Long personId, String clubName);

    /**
     * Deletes if the specified relationship exists.
     * @param personId - The Person to check.
     * @param clubName - The Club to check.
     */
    void deleteByIdPersonIdAndIdClubName(Long personId, String clubName);
}
