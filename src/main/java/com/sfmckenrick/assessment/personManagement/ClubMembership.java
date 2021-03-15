package com.sfmckenrick.assessment.personManagement;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * The Join entity to store the Many-to-Many relationship between Person and Club.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
@Entity
@Table(name = "club_membership")
public class ClubMembership {

    /**
     * Composite Key.
     */
    @EmbeddedId
    private final ClubMembershipKey id = new ClubMembershipKey();

    /**
     * The Person involved in this relationship.
     */
    @ManyToOne
    @MapsId("personId")
    @JoinColumn(name = "person_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Person person;

    /**
     * The club in which this person belongs.
     */
    @ManyToOne
    @MapsId("clubName")
    @JoinColumn(name = "club_name")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Club club;

    /**
     * Serialization constructor.
     */
    public ClubMembership() {

    }

    /**
     * Constructor.
     * @param person - The person that belongs to a club.
     * @param club - The club where a person belongs.
     */
    public ClubMembership(Person person, Club club) {
        this.club = club;
        this.person = person;
    }

    /**
     * Accessor for the Person.
     * @return The person that belongs to a Club.
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Accessor for the Club.
     * @return The Person to which the Person belongs.
     */
    public Club getClub() {
        return club;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(obj);
    }

}
