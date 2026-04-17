package co.edu.cun.tienda.tienda.adapter.rest.v1.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.cun.tienda.tienda.adapter.rest.v1.controller.DTO.AdapterProducts;
import co.edu.cun.tienda.tienda.adapter.rest.v1.controller.DTO.PersonExternalAdapter;
import co.edu.cun.tienda.tienda.domain.DTO.PersonExternalDomain;
import co.edu.cun.tienda.tienda.domain.DTO.ProductsDomain;

@Mapper(componentModel = "spring")
public interface AdapterMapper {

    @Mapping(target = "identificator", source = "id")
    @Mapping(target = "productName", source = "nombre")
    @Mapping(target = "productDescription", source = "descripcion")
    @Mapping(target = "productPrice", source = "precio")
    @Mapping(target = "productImage", source = "imagen")
    @Mapping(target = "productCategory", source = "categoria")
    @Mapping(target = "personas", source = "person")
    AdapterProducts toAdapterProducts(ProductsDomain productsDomain);

    @Mapping(target = "id", source = "identificator")
    @Mapping(target = "nombre", source = "productName")
    @Mapping(target = "descripcion", source = "productDescription")
    @Mapping(target = "precio", source = "productPrice")
    @Mapping(target = "imagen", source = "productImage")
    @Mapping(target = "categoria", source = "productCategory")
    @Mapping(target = "person", ignore = true)
    ProductsDomain toProductsDomain(AdapterProducts adapterProducts);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "personName", source = "name")
    @Mapping(target = "personEmail", source = "email")
    @Mapping(target = "personPhone", source = "phone")
    @Mapping(target = "personAddress", source = "address")
    @Mapping(target = "personCity", source = "city")
    PersonExternalAdapter toPersonExternalAdapter(PersonExternalDomain personExternalDomain);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "personName")
    @Mapping(target = "email", source = "personEmail")
    @Mapping(target = "phone", source = "personPhone")
    @Mapping(target = "address", source = "personAddress")
    @Mapping(target = "city", source = "personCity")
    PersonExternalDomain toPersonExternalDomain(PersonExternalAdapter personExternalAdapter);

}
