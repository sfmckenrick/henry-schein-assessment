package com.sfmckenrick.assessment.personManagement;

import javax.persistence.*;

/**
 * The entity object that represents a club.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
@Entity
@Table(name = "club")
public class Club {

    /**
     * Auto-generated identifier.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The street name and number of this Address.
     */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * The city name of this Address.
     */
    @Column
    private String description;


    /**
     * Serialization Constructor.
     */
    public Club() {}

    /**
     * Constructor.
     * @param name - The name of this club.
     * @param description - The description of the club.
     */
    public Club(String name, String description) {
        this.name = name;
        this.description = description;
    }
    /**
     * ID Accessor.
     * @return The primary key identifier.
     */
    public Long getId() {
        return id;
    }

    /**
     * Club name accessor.
     * @return The name of the club.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the club.
     * @param name - The name of the club to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Club description accessor.
     * @return The description of the club.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the club's description.
     * @param description - The descirption of the club to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
