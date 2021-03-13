package com.sfmckenrick.assessment.personManagement;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Person Entity Object to be persisted to the database
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
@Entity
@Table(name = "person")
public class Person {

    /**
     * Auto-generated identifier.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * First Name of this person.
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * Middle Name of this person.
     */
    @Column(name = "middle_name")
    private String middleName;

    /**
     * Last Name of this person.
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * DoB of this person.
     */
    @Column(name = "dob", nullable = false)
    private Date dateOfBirth;

    /**
     * Constructor for serialization.
     */
    public Person() {}

    /**
     * Constructor.
     * @param firstName - First name of the person.
     * @param lastName - Last name of the person.
     * @param dateOfBirth - DoB of the person.
     */
    public Person(String firstName, String lastName, Date dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * ID Accessor.
     * @return The primary key identifier.
     */
    public Long getId() {
        return id;
    }

    /**
     * First Name Accessor.
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the First name to the supplied value.
     * @param firstName - The first name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Middle Name Accessor.
     * @return The middle name.
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the middle name to the supplied value.
     * @param middleName - The middle name to set.
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * Last Name Accessor.
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name to the supplied value.
     * @param lastName - The last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Date of Birth Name Accessor.
     * @return The DoB.
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the Date of Birth to the supplied value.
     * @param dateOfBirth - The date object that represents the date of birth.
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person that = (Person) o;
        return getId().equals(that.getId())
                && getFirstName().equals(that.getFirstName())
                && Objects.equals(getMiddleName(), that.getMiddleName())
                && getLastName().equals(that.getLastName())
                && getDateOfBirth().equals(that.getDateOfBirth());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getMiddleName(), getLastName(), getDateOfBirth());
    }
}
