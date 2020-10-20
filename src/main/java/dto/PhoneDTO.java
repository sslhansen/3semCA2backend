package dto;

import entities.Person;
import entities.Phone;

public class PhoneDTO {

    private int number;
    private String description;
    private Person person;

    public PhoneDTO(Phone phone) {
        this.number = phone.getNumber();
        this.description = phone.getDescription();
        this.person = phone.getPerson();
    }

    public Person getPerson() {
        return person;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
