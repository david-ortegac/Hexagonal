package co.edu.cun.tienda.tienda.domain;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import co.edu.cun.tienda.tienda.application.IProducts;
import co.edu.cun.tienda.tienda.domain.DTO.PersonExternalDomain;
import co.edu.cun.tienda.tienda.domain.DTO.ProductsDomain;
import co.edu.cun.tienda.tienda.domain.exception.ExternalServiceException;
import co.edu.cun.tienda.tienda.domain.exception.ProductNotFoundException;
import co.edu.cun.tienda.tienda.infraestrucure.Entity.Products;
import co.edu.cun.tienda.tienda.infraestrucure.Repository.IProductsRespository;
import co.edu.cun.tienda.tienda.infraestrucure.mapper.InfraMapper;
import jakarta.transaction.Transactional;

@Service
public class ProductsImpl implements IProducts {

    private final IProductsRespository repository;
    private final InfraMapper mapper;
    private final RestClient rest;

    public ProductsImpl(IProductsRespository repository, InfraMapper mapper, RestClient rest) {
        this.repository = repository;
        this.mapper = mapper;
        this.rest = rest;
    }

    @Override
    @Transactional
    public ProductsDomain createProduct(ProductsDomain productsDomain) {
        Products products = mapper.toProducts(productsDomain);
        products = repository.save(products);
        return mapper.toProductsDomain(products);
    }

    @Override
    public ProductsDomain getProductByIdPersonById(Long id, Long personId) {
        Products products = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        PersonExternalDomain person = this.getPersonById(personId);

        ProductsDomain productsDomain = mapper.toProductsDomain(products);
        productsDomain.setPerson(person);
        return productsDomain;
    }

    @Override
    @Transactional
    public ProductsDomain updateProduct(ProductsDomain productsDomain, Long id) {
        Products products = mapper.toProducts(this.getProductById(id));
        products.setName(productsDomain.getNombre());
        products.setDescription(productsDomain.getDescripcion());
        products.setPrice(productsDomain.getPrecio());
        products.setImage(productsDomain.getImagen());
        products.setCategory(productsDomain.getCategoria());
        products = repository.save(products);
        return mapper.toProductsDomain(products);
    }

    @Override
    @Transactional
    public String deleteProduct(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Product deleted successfully";
        }
        return "Product not deleted successfully";
    }

    @Override
    public PersonExternalDomain getPersonById(Long id) {
        try {
            PersonExternalDomain person = rest.get()
                    .uri("/persons/{id}", id)
                    .header("Content-Type", "application/json")
                    .retrieve()
                    .body(PersonExternalDomain.class);
            if (person == null) {
                throw new ExternalServiceException("Person with id " + id + " not found", HttpStatus.NOT_FOUND);
            }
            return person;
        } catch (ExternalServiceException e) {
            throw e;
        } catch (HttpClientErrorException.NotFound e) {
            throw new ExternalServiceException("Person with id " + id + " not found", HttpStatus.NOT_FOUND);
        } catch (HttpClientErrorException e) {
            throw new ExternalServiceException(
                "Client error calling persons service: " + e.getStatusCode().value(),
                e.getStatusCode());
        } catch (HttpServerErrorException e) {
            throw new ExternalServiceException(
                "Persons service is unavailable (status " + e.getStatusCode().value() + "). Please try again later.",
                e.getStatusCode());
        } catch (ResourceAccessException e) {
            throw new ExternalServiceException(
                "Could not reach persons service: " + e.getMessage(),
                HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            throw new ExternalServiceException(
                "Unexpected error calling persons service: " + e.getMessage(),
                HttpStatus.BAD_GATEWAY);
        }
    }

    @Override
    public ProductsDomain getProductById(Long id) {
        return repository.findById(id).map(mapper::toProductsDomain)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

}
