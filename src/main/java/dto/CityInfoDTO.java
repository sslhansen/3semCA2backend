package dto;

import entities.CityInfo;


public class CityInfoDTO {
    private Long id;
    private int zipCode;
    private String city;

    public CityInfoDTO(CityInfo cityInfo) {
        this.id = cityInfo.getId();
        this.zipCode = cityInfo.getZipCode();
        this.city = cityInfo.getCity();
    }

    public CityInfoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
}
