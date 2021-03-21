package com.vvs.ecommerce.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.vvs.ecommerce.entity.Product;
import com.vvs.ecommerce.repository.ProductRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

public class ProductsControllerTest {
    
    @Test
    public void list_ReturnsValidModelAndView() {
        List<Product> products = IntStream.range(1, 4)
            .mapToObj(i -> new Product(UUID.randomUUID(), "Product #" + i, "Product #" + 1 + " description"))
            .collect(Collectors.toList());

        ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        Mockito.doReturn(products).when(productRepository).findAll();

        ProductController productController = new ProductController(productRepository);
        
        ModelAndView modelAndView = productController.list();

        Assertions.assertNotNull(modelAndView);
        Assertions.assertEquals(products, modelAndView.getModel().get("products"));
        Assertions.assertEquals("/catalog/products/list", modelAndView.getViewName());
        Assertions.assertEquals(HttpStatus.OK, modelAndView.getStatus());

        Mockito.verify(productRepository).findAll();
    } 

}
