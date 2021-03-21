package com.vvs.ecommerce.repository;

import java.util.List;

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
    };
}
