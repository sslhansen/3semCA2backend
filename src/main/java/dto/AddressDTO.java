package dto;

import entities.Address;

public class AddressDTO {

    private String street;
    private String additionalInfo;
    private CityInfoDTO cityinfo;

    public AddressDTO(Address address) {
        this.street = address.getStreet();
        this.additionalInfo = address.getAdditionalInfo();
        this.cityinfo = new CityInfoDTO(address.getCityinfo());

    }

    public AddressDTO() {
    }

    public CityInfoDTO getCityinfo() {
        return cityinfo;
    }

    public void setCityinfo(CityInfoDTO cityinfo) {
        this.cityinfo = cityinfo;
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
