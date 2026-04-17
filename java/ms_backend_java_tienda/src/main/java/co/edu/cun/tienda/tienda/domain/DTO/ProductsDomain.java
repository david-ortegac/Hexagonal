package co.edu.cun.tienda.tienda.domain.DTO;

import lombok.Data;

@Data
public class ProductsDomain {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String imagen;
    private String categoria;
    private PersonExternalDomain person;
}
