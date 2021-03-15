package com.sfmckenrick.assessment.personManagement;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Composite Key for the Club - Person mapping.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
@Embeddable
public class ClubMembershipKey implements Serializable {

    /**
     * The Serialization UID.
     */
    private static final long serialVersionUID = 3191530267155010873L;

    @Column(name = "person_id", nullable = false)
    Long personId;

    @Column(name = "club_name", nullable = false)
    String clubName;

    /**
     * Serialization constructor.
     */
    public ClubMembershipKey() {

    }

    /**
     * Constructor.
     * @param personId - The person ID to map.
     * @param clubName - The Club ID to map.
     */
    public ClubMembershipKey(long personId, String clubName) {
        this.personId = personId;
        this.clubName = clubName;
    }

    /**
     * Accessor for the Person ID.
     * @return The ID of the mapped Person.
     */
    public Long getPersonId() {
        return personId;
    }

    /**
     * Sets the Person ID for this mapping
     * @param personId - The Person ID to set.
     */
    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    /**
     * Accessor for the Club ID.
     * @return The name of the mapped Club.
     */
    public String getclubName() {
        return clubName;
    }

    /**
     * Sets the Club name for this mapping
     * @param clubName - The Club name to set.
     */
    public void setclubName(String clubName) {
        this.clubName = clubName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClubMembershipKey that = (ClubMembershipKey) o;
        return getPersonId().equals(that.getPersonId()) && getclubName().equals(that.getclubName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPersonId(), getclubName());
    }

}
