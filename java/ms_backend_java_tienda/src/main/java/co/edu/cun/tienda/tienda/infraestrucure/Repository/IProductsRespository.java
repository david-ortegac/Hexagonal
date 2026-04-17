package co.edu.cun.tienda.tienda.infraestrucure.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.cun.tienda.tienda.infraestrucure.Entity.Products;

public interface IProductsRespository extends JpaRepository<Products, Long> {
    
}
