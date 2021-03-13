package com.sfmckenrick.assessment.personManagement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Test Suite to test the PersonManagementService.
 * @author Scott McKenrick <sbm5967@arl.psu.edu>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonManagementServiceTest {

    @Autowired
    private PersonManagementService service;

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
        Assertions.assertTrue(service.getPersonById(p.getId()).isPresent());
        service.deletePersonById(p.getId());
        Assertions.assertTrue(service.getPersonById(p.getId()).isEmpty());
    }

    @Test
    public void testDeletePersonSingleAddress() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Address a1 = new Address("123 Street", "Cityville", State.ALABAMA, "12345", p);
        service.saveAddress(a1);

        service.deletePersonById(p.getId());

        Assertions.assertTrue(service.getPersonById(p.getId()).isEmpty());
        Assertions.assertTrue(service.getAddressById(a1.getId()).isEmpty());
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

        Assertions.assertTrue(service.getPersonById(p.getId()).isEmpty());
        Assertions.assertTrue(service.getAddressById(a1.getId()).isEmpty());
        Assertions.assertTrue(service.getAddressById(a2.getId()).isEmpty());

    }

    @Test
    public void testGetPerson() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Optional<Person> maybePerson = service.getPersonById(p.getId());
        Assertions.assertEquals(p, maybePerson.get());
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
        Assertions.assertTrue(service.getAddressById(a1.getId()).isEmpty());
        Assertions.assertTrue(service.getAddressById(a2.getId()).isEmpty());

    }

    @Test
    public void testDeleteAddress() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Address a1 = new Address("123 Street", "Cityville", State.ALABAMA, "12345", p);
        service.saveAddress(a1);

        service.deleteAddressById(a1.getId());
        Assertions.assertTrue(service.getAddressById(a1.getId()).isEmpty());
        Assertions.assertTrue(service.getPersonById(p.getId()).isPresent());

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

        Assertions.assertTrue(service.getAddressById(a1.getId()).isPresent());
        Assertions.assertTrue(service.getAddressById(a2.getId()).isPresent());


    }

    @Test
    public void testGetAddress() {
        Person p = new Person("John", null, "Doe", new Date(System.currentTimeMillis()));
        service.savePerson(p);

        Address a1 = new Address("123 Street", "Cityville", State.ALABAMA, "12345", p);
        service.saveAddress(a1);

        Optional<Address> maybeAddress = service.getAddressById(a1.getId());
        Assertions.assertEquals(a1, maybeAddress.get());

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
}
