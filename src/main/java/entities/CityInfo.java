package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;


@Entity
@NamedQueries({
    @NamedQuery(name = "CityInfo.deleteAllRows", query = "DELETE from CityInfo"),
})
public class CityInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int zipCode;
    private String city;
    
    @OneToMany(mappedBy = "cityinfo", cascade = CascadeType.PERSIST)
    List<Address> addresses;

    public CityInfo(int zipCode, String city) {
        this.zipCode = zipCode;
        this.city = city;
        this.addresses = new ArrayList<>();
    }
    
    
    public CityInfo() {
    }
    
    
    public List<Address> getAddresses() {
        return addresses;
    }
    
    public void addAddress(Address address) {
        this.addresses.add(address);
        if (address != null) {
            address.setCityinfo(this);
        }
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
