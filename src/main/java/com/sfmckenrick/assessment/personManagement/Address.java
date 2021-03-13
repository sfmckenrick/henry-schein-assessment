package com.sfmckenrick.assessment.personManagement;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
@Entity
@Table(name = "address")
public class Address {
    /**
     * Auto-generated identifier.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The street name and number of this Address.
     */
    @Column(nullable = false)
    private String street;

    /**
     * The city name of this Address.
     */
    @Column(nullable = false)
    private String city;

    /**
     * The state of this address
     */
    @Column(nullable = false)
    private State state;

    /**
     * The zipcode of this address.
     */
    @Column(nullable = false)
    private String zipcode;

    /**
     * The Person where this address belongs.
     */
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Person person;

    /**
     * Constructor for serialization.
     */
    public Address() {}

    /**
     * Constructor.
     * @param street - Street address.
     * @param city - The city of the address.
     * @param state - The state where this address belongs.
     * @param zipcode - The zipcode of this address.
     * @param person - The person where this address belongs.
     */
    public Address(String street, String city, State state, String zipcode, Person person) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.person = person;
    }

    /**
     * ID Accessor.
     * @return The primary key identifier.
     */
    public Long getId() {
        return id;
    }

    /**
     * Street address accessor.
     * @return The street address.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street address.
     * @param street - The street address to set.
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * City name accessor.
     * @return The city name.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the City name.
     * @param city - The city name to set.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * State Accessor.
     * @return The state of the address.
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the state of the address.
     * @param state - The state enumeration value to set.
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Zipcode Accessor.
     * @return The zipcode of the address.
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * Sets the Zipcode of the address.
     * @param zipcode - The zipcode to set.
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * Person Accessor.
     * @return The person who resides at this address.
     */
    public Person getPerson() {
        return person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return getId().equals(address.getId())
                && getStreet().equals(address.getStreet())
                && getCity().equals(address.getCity())
                && getState() == address.getState()
                && getZipcode().equals(address.getZipcode())
                && getPerson().equals(address.getPerson());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStreet(), getCity(), getState(), getZipcode());
    }


}
