package dto;

import entities.Address;

public class AddressDTO {

    private Long id;
    private String street;
    private String additionalInfo;

    public AddressDTO(Address adress) {
        this.id = adress.getId();
        this.street = adress.getStreet();
        this.additionalInfo = adress.getAdditionalInfo();
    }
    
    public AddressDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
