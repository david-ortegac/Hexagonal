package co.edu.cun.tienda.tienda.application;

import co.edu.cun.tienda.tienda.domain.DTO.PersonExternalDomain;
import co.edu.cun.tienda.tienda.domain.DTO.ProductsDomain;

public interface IProducts {
    ProductsDomain createProduct(ProductsDomain productsDomain);
    ProductsDomain getProductById(Long id);
    ProductsDomain getProductByIdPersonById(Long id, Long personId);
    ProductsDomain updateProduct(ProductsDomain productsDomain, Long id);
    String deleteProduct(Long id);
    PersonExternalDomain getPersonById(Long idPerson);
} 