package co.edu.cun.ms_backend_java_persons.persons.domain.DTO;

import lombok.Data;

@Data
public class PersonDomain {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String updatedAt;
    private String createdAt;
}
