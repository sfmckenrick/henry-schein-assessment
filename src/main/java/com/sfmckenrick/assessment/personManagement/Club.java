package com.sfmckenrick.assessment.personManagement;

import javax.persistence.*;
import java.util.Objects;

/**
 * The entity object that represents a club.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
@Entity
@Table(name = "club")
public class Club {

    /**
     * The Name of the club.
     */
    @Id
    @Column
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Club club = (Club) o;
        return getName().equals(club.getName()) && Objects.equals(getDescription(), club.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription());
    }

    /**
     * The Description of this club.
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
