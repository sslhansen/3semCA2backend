package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String firstName;
    private String lastName;

    @ManyToMany(mappedBy = "persons", cascade = CascadeType.PERSIST)
    List<Hobby> hobbies;

    @OneToMany(mappedBy = "person", cascade = CascadeType.PERSIST)
    List<Phone> phones;
    
    @ManyToOne
    private Address address;
    
    public Person(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hobbies = new ArrayList<>();
        this.phones = new ArrayList<>();
    }
    
    public Person() {
    }
    
    public void addHobby(Hobby hobby){
        if (hobby != null){
            this.hobbies.add(hobby);
            hobby.getPersons().add(this);
        }
    }
    
    public void removeHobby(Hobby hobby){
        if (hobby != null){
            this.hobbies.remove(hobby);
            hobby.getPersons().remove(this);
        }
    }

    public List<Phone> getPhones() {
        return phones;
    }
    
    public void addPhone(Phone phone) {
        this.phones.add(phone);
        if (phone != null) {
            phone.setPerson(this);
        }
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    
    
    
}
