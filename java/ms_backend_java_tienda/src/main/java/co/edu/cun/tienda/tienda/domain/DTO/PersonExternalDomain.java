package co.edu.cun.tienda.tienda.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonExternalDomain {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
}
