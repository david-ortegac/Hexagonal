package co.edu.cun.ms_backend_java_persons.persons.adapter.restfull.v1.controller.DTO;

import lombok.Data;

@Data
public class PersonAdapter {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String updatedAt;
    private String createdAt;
}
