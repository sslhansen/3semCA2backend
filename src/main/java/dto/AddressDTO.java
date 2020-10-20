package dto;

import entities.Address;
import entities.Person;
import java.util.List;

public class AddressDTO {

    private String street;
    private String additionalInfo;
    private List<Person> persons;

    public AddressDTO(Address address) {
        this.street = address.getStreet();
        this.additionalInfo = address.getAdditionalInfo();
        this.persons = address.getPersons();
    }

    public AddressDTO() {
    }

    public List<Person> getPersons() {
        return persons;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

}
