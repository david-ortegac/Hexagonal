package co.edu.cun.tienda.tienda.adapter.rest.v1.controller.DTO;

import lombok.Data;

@Data
public class PersonExternalAdapter{
    private int id;
    private String personName;
    private String personEmail;
    private String personPhone;
    private String personAddress;
    private String personCity;

}