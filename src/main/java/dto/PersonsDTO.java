/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author groen
 */
public class PersonsDTO {
    
    public List<PersonDTO> getAll() {
        return all;
    }
    public void setAll(List<PersonDTO> all){
        this.all = all;
    }
    List<PersonDTO> all = new ArrayList();
    

}