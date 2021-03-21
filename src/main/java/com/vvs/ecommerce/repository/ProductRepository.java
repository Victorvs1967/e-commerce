package com.vvs.ecommerce.repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.vvs.ecommerce.entity.Product;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Repository
public class ProductRepository {

    private List<Product> products;

    public List<Product> findAll() {
        return this.products;
    }

    public Optional<Product> findOneById(UUID id) {
        return this.products.stream()
                .filter(product -> Objects.equals(id, product.getId()))
                .findFirst();
    };
}
