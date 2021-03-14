package com.sfmckenrick.assessment.personManagement;

import com.sfmckenrick.assessment.personManagement.exception.AddressNotFoundException;
import com.sfmckenrick.assessment.personManagement.exception.ClubNotFoundException;
import com.sfmckenrick.assessment.personManagement.exception.PersonNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Date;
import java.util.List;

/**
 * Test Suite to test the PersonManagementService.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
@SpringBootTest
public class PersonManagementServiceTest {

    @Autowired
    private PersonManagementService service;

    @BeforeEach
    public void clean() {
        service.clear();
    }

    @Test
    public void testAddPerson() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        Assertions.assertNull(p.getId());
        service.savePerson(p);
        Assertions.assertNotNull(p.getId());
    }

    @Test
    public void testUpdatePerson() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);
        Assertions.assertNotNull(p.getId());
        long id = p.getId();
        p.setMiddleName("something");
        service.savePerson(p);
        Assertions.assertEquals("something", p.getMiddleName());
        Assertions.assertEquals(id, p.getId());
    }

    @Test
    public void testDeletePersonNoAddress() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);
        Assertions.assertNotNull(service.getPersonById(p.getId()));
        service.deletePersonById(p.getId());
        Assertions.assertThrows(PersonNotFoundException.class, () -> service.getPersonById(p.getId()));
    }

    @Test
    public void testDeletePersonSingleAddress() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Address a1 = new Address("123 Street", "Cityville", State.ALABAMA, "12345", p);
        service.saveAddress(a1);

        service.deletePersonById(p.getId());

        Assertions.assertThrows(PersonNotFoundException.class, () -> service.getPersonById(p.getId()));
        Assertions.assertThrows(AddressNotFoundException.class, () -> service.getAddressById(a1.getId()));
    }

    @Test
    public void testDeletePersonMultipleAddress() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Address a1 = new Address("123 Street", "Cityville", State.ALABAMA, "12345", p);
        service.saveAddress(a1);
        Address a2 = new Address("456 Blvd", "Townsville", State.OKLAHOMA, "98765", p);
        service.saveAddress(a2);
        service.deletePersonById(p.getId());

        Assertions.assertThrows(PersonNotFoundException.class, () -> service.getPersonById(p.getId()));
        Assertions.assertThrows(AddressNotFoundException.class, () -> service.getAddressById(a1.getId()));
        Assertions.assertThrows(AddressNotFoundException.class, () -> service.getAddressById(a2.getId()));

    }

    @Test
    public void testGetPerson() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Person result = service.getPersonById(p.getId());
        Assertions.assertEquals(p, result);
    }

    @Test
    public void testAddAddress() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Address a1 = new Address("123 Street", "Cityville", State.ALABAMA, "12345", p);
        service.saveAddress(a1);

        Assertions.assertNotNull(a1.getId());
    }

    @Test
    public void testDeleteAddressByPersonId() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Address a1 = new Address("123 Street", "Cityville", State.ALABAMA, "12345", p);
        service.saveAddress(a1);
        Address a2 = new Address("456 Blvd", "Townsville", State.OKLAHOMA, "98765", p);
        service.saveAddress(a2);

        service.deleteAllAddressForPersonById(p.getId());
        Assertions.assertThrows(AddressNotFoundException.class, () -> service.getAddressById(a1.getId()));
        Assertions.assertThrows(AddressNotFoundException.class, () -> service.getAddressById(a2.getId()));

    }

    @Test
    public void testDeleteAddress() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Address a1 = new Address("123 Street", "Cityville", State.ALABAMA, "12345", p);
        service.saveAddress(a1);

        service.deleteAddressById(a1.getId());
        Assertions.assertThrows(AddressNotFoundException.class, () -> service.getAddressById(a1.getId()));
        Assertions.assertNotNull(service.getPersonById(p.getId()));

    }

    @Test
    public void testUpdateAddress() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Address a1 = new Address("123 Street", "Cityville", State.ALABAMA, "12345", p);
        service.saveAddress(a1);

        long id = a1.getId();
        a1.setCity("NewTown");
        service.saveAddress(a1);

        Assertions.assertEquals(id, a1.getId());
        Assertions.assertEquals("NewTown", a1.getCity());
    }

    @Test
    public void testAddMultipleAddress() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Address a1 = new Address("123 Street", "Cityville", State.ALABAMA, "12345", p);
        service.saveAddress(a1);
        Address a2 = new Address("456 Blvd", "Townsville", State.OKLAHOMA, "98765", p);
        service.saveAddress(a2);

        Assertions.assertNotNull(service.getAddressById(a1.getId()));
        Assertions.assertNotNull(service.getAddressById(a2.getId()));


    }

    @Test
    public void testGetAddress() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Address a1 = new Address("123 Street", "Cityville", State.ALABAMA, "12345", p);
        service.saveAddress(a1);

        Address result = service.getAddressById(a1.getId());
        Assertions.assertEquals(a1, result);

    }

    @Test
    public void testGetAddressByPerson() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Address a1 = new Address("123 Street", "Cityville", State.ALABAMA, "12345", p);
        service.saveAddress(a1);
        Address a2 = new Address("456 Blvd", "Townsville", State.OKLAHOMA, "98765", p);
        service.saveAddress(a2);

        List<Address> addressList = service.getAddressByPersonId(p.getId());

        Assertions.assertEquals(2, addressList.size());

        Assertions.assertTrue(addressList.contains(a1));
        Assertions.assertTrue(addressList.contains(a2));
    }

    @Test
    public void testAddClub() {
        Club club = new Club("Test Club", null);
        service.saveClub(club);
    }

    @Test
    public void testGetClubByName() {
        Club club = new Club("Test Club", null);
        service.saveClub(club);
        Club result = service.getClubByName(club.getName());
        Assertions.assertEquals(club, result);
    }

    @Test
    public void testUpdateClub() {
        Club club = new Club("Test Club", null);
        service.saveClub(club);
        String name = club.getName();

        club.setDescription("new");
        service.saveClub(club);
        Assertions.assertEquals(name, club.getName());
        Assertions.assertEquals("new", club.getDescription());
    }

    @Test
    public void testDeleteClub() {
        Club club = new Club("Test Club", null);
        service.saveClub(club);
        service.deleteClubByName(club.getName());
        Assertions.assertThrows(ClubNotFoundException.class, () -> service.getClubByName(club.getName()));
    }

    @Test
    public void testAddClubMembership() {
        Club club = new Club("Test Club", null);
        service.saveClub(club);

        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        ClubMembership membership = service.addClubMembership(p.getId(), club.getName());
        Assertions.assertEquals(club, membership.getClub());
        Assertions.assertEquals(p, membership.getPerson());
        Assertions.assertTrue(service.isPersonClubMember(p.getId(), club.getName()));
    }

    @Test
    public void testAddDuplicateClubMembership() {
        Club club = new Club("Test Club", null);
        service.saveClub(club);

        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        ClubMembership membership = service.addClubMembership(p.getId(), club.getName());
        Assertions.assertNotNull(membership.getClub());
        Assertions.assertNotNull(membership.getPerson());

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> service.addClubMembership(p.getId(), club.getName()));
    }

    @Test
    public void testDeleteClubMembership() {
        Club club = new Club("Test Club", null);
        service.saveClub(club);

        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        ClubMembership membership = service.addClubMembership(p.getId(), club.getName());
        Assertions.assertEquals(club, membership.getClub());
        Assertions.assertEquals(p, membership.getPerson());

        service.deleteClubMembership(p.getId(), club.getName());
        Assertions.assertFalse(service.isPersonClubMember(p.getId(), club.getName()));
        Assertions.assertNotNull(service.getPersonById(p.getId()));
        Assertions.assertNotNull(service.getClubByName(club.getName()));
    }

    @Test
    public void testDeleteClubMembershipByPerson() {
        Club club = new Club("Test Club", null);
        service.saveClub(club);

        Club club2 = new Club("Test Club 2", null);
        service.saveClub(club2);

        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        ClubMembership membership = service.addClubMembership(p.getId(), club.getName());
        ClubMembership membership2 = service.addClubMembership(p.getId(), club2.getName());

        Assertions.assertTrue(service.isPersonClubMember(p.getId(), club.getName()));
        Assertions.assertTrue(service.isPersonClubMember(p.getId(), club2.getName()));

        service.deleteClubMembershipByPerson(p.getId());
        Assertions.assertFalse(service.isPersonClubMember(p.getId(), club.getName()));
        Assertions.assertFalse(service.isPersonClubMember(p.getId(), club2.getName()));
    }

    @Test
    public void testDeleteClubMembershipByClub() {
        Club club = new Club("Test Club", null);
        service.saveClub(club);

        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Person p2 = new Person("John", "William", "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p2);

        ClubMembership membership = service.addClubMembership(p.getId(), club.getName());
        ClubMembership membership2 = service.addClubMembership(p2.getId(), club.getName());

        Assertions.assertTrue(service.isPersonClubMember(p.getId(), club.getName()));
        Assertions.assertTrue(service.isPersonClubMember(p2.getId(), club.getName()));

        service.deleteClubMembershipByClub(club.getName());
        Assertions.assertFalse(service.isPersonClubMember(p.getId(), club.getName()));
        Assertions.assertFalse(service.isPersonClubMember(p2.getId(), club.getName()));
    }

    @Test
    public void testGetClubMembershipByPerson() {
        Club club = new Club("Test Club", null);
        service.saveClub(club);

        Club club2 = new Club("Test Club 2", null);
        service.saveClub(club2);

        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        ClubMembership membership = service.addClubMembership(p.getId(), club.getName());
        ClubMembership membership2 = service.addClubMembership(p.getId(), club2.getName());

        Assertions.assertTrue(service.isPersonClubMember(p.getId(), club.getName()));
        Assertions.assertTrue(service.isPersonClubMember(p.getId(), club2.getName()));

        List<Club> clubs = service.getClubMembershipForPerson(p.getId());
        Assertions.assertEquals(2, clubs.size());
        Assertions.assertTrue(clubs.contains(club));
        Assertions.assertTrue(clubs.contains(club2));
    }

    @Test
    public void testGetClubMembershipByClub() {
        Club club = new Club("Test Club", null);
        service.saveClub(club);

        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Person p2 = new Person("John", "William", "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p2);

        ClubMembership membership = service.addClubMembership(p.getId(), club.getName());
        ClubMembership membership2 = service.addClubMembership(p2.getId(), club.getName());

        List<Person> persons = service.getPersonMembersForClub(club.getName());

        Assertions.assertEquals(2, persons.size());
        Assertions.assertTrue(persons.contains(p));
        Assertions.assertTrue(persons.contains(p2));
    }

    @Test
    public void testCascadeDeletePerson() {
        Club club = new Club("Test Club", null);
        service.saveClub(club);

        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Address a1 = new Address("123 Street", "Cityville", State.ALABAMA, "12345", p);
        service.saveAddress(a1);

        ClubMembership membership = service.addClubMembership(p.getId(), club.getName());

        service.deletePersonById(p.getId());

        Assertions.assertNotNull(service.getClubByName(club.getName()));
        Assertions.assertThrows(AddressNotFoundException.class, () -> service.getAddressById(a1.getId()));
        Assertions.assertThrows(PersonNotFoundException.class, () -> service.getPersonById(p.getId()));
        Assertions.assertFalse(service.isPersonClubMember(p.getId(), club.getName()));
    }

    @Test
    public void testCascadeDeleteClub() {
        Club club = new Club("Test Club", null);
        service.saveClub(club);

        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Address a1 = new Address("123 Street", "Cityville", State.ALABAMA, "12345", p);
        service.saveAddress(a1);

        ClubMembership membership = service.addClubMembership(p.getId(), club.getName());

        service.deleteClubByName(club.getName());

        Assertions.assertThrows(ClubNotFoundException.class, () -> service.getClubByName(club.getName()));
        Assertions.assertNotNull(service.getAddressById(a1.getId()));
        Assertions.assertNotNull(service.getPersonById(p.getId()));
        Assertions.assertFalse(service.isPersonClubMember(p.getId(), club.getName()));
    }


}
