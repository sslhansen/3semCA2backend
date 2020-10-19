package dto;

import entities.Phone;


public class PhoneDTO {
    private Long id;
    private int number;
    private String description;

    public PhoneDTO(Phone phone) {
        this.id = phone.getId();
        this.number = phone.getNumber();
        this.description = phone.getDescription();
    }
    
    
    
    
}
