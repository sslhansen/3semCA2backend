package dto;

import entities.Hobby;
import entities.Person;
import java.util.List;

public class HobbyDTO {

    private Long id;
    private String name;
    private String link;
    private String type;
    private String description;
    private List<Person> persons;

    public HobbyDTO(Hobby hobby) {
        this.id = hobby.getId();
        this.name = hobby.getName();
        this.link = hobby.getLink();
        this.type = hobby.getType();
        this.description = hobby.getDescription();
        this.persons = hobby.getPersons();
    }

    public HobbyDTO() {
    }

    public List<Person> getPersons() {
        return persons;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
