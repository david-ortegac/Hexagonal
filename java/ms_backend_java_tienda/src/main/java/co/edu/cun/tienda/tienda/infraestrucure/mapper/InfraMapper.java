package co.edu.cun.tienda.tienda.infraestrucure.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.cun.tienda.tienda.domain.DTO.ProductsDomain;
import co.edu.cun.tienda.tienda.infraestrucure.Entity.Products;

@Mapper(componentModel = "spring")
public interface InfraMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "name")
    @Mapping(target = "descripcion", source = "description")
    @Mapping(target = "precio", source = "price")
    @Mapping(target = "imagen", source = "image")
    @Mapping(target = "categoria", source = "category")
    @Mapping(target="person", ignore = true)
    ProductsDomain toProductsDomain(Products products);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "nombre")
    @Mapping(target = "description", source = "descripcion")
    @Mapping(target = "price", source = "precio")
    @Mapping(target = "image", source = "imagen")
    @Mapping(target = "category", source = "categoria")
    Products toProducts(ProductsDomain productsDomain);

    List<ProductsDomain> toProductsDomainList(List<Products> products);
    
    @Mapping(target = "person", ignore = true)
    List<Products> toProductsList(List<ProductsDomain> productsDomain);
}
