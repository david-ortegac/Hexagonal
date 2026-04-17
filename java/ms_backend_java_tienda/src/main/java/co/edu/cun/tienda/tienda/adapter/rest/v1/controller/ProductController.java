package co.edu.cun.tienda.tienda.adapter.rest.v1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.cun.tienda.tienda.adapter.rest.v1.controller.DTO.AdapterProducts;
import co.edu.cun.tienda.tienda.adapter.rest.v1.controller.DTO.PersonExternalAdapter;
import co.edu.cun.tienda.tienda.adapter.rest.v1.controller.mapper.AdapterMapper;
import co.edu.cun.tienda.tienda.application.IProducts;
import co.edu.cun.tienda.tienda.domain.DTO.ProductsDomain;


@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final IProducts productsService;
    private final AdapterMapper adapterMapper;

    public ProductController(IProducts productsService, AdapterMapper adapterMapper) {
        this.productsService = productsService;
        this.adapterMapper = adapterMapper;
    }

    @PostMapping("/")
    public ResponseEntity<AdapterProducts> createProduct(@RequestBody AdapterProducts adapterProducts) {
        ProductsDomain productsDomain = adapterMapper.toProductsDomain(adapterProducts);
        productsDomain = productsService.createProduct(productsDomain);
        return ResponseEntity.ok(adapterMapper.toAdapterProducts(productsDomain));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdapterProducts> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(adapterMapper.toAdapterProducts(productsService.getProductById(id)));
    }
    

    @GetMapping("/{id}/{personId}")
    public ResponseEntity<AdapterProducts> getProductByIdPersonById(@PathVariable("id") Long id, @PathVariable("personId") Long personId) {
        return ResponseEntity.ok(adapterMapper.toAdapterProducts(productsService.getProductByIdPersonById(id, personId)));
    }

    @PutMapping("/{id}/{personId}")
    public ResponseEntity<AdapterProducts> updateProduct(@PathVariable("id") Long id, @RequestBody AdapterProducts adapterProducts) {
        return ResponseEntity.ok(adapterMapper.toAdapterProducts(productsService.updateProduct(adapterMapper.toProductsDomain(adapterProducts), id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productsService.deleteProduct(id));
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<PersonExternalAdapter> getPersonById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(adapterMapper.toPersonExternalAdapter(productsService.getPersonById(id)));
    }
}
