package com.vvs.ecommerce.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.vvs.ecommerce.entity.Product;
import com.vvs.ecommerce.repository.ProductRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

public class ProductsControllerTest {

    private ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private ProductController productController = new ProductController(productRepository);

    @Test
    public void product_ProductExist_ReturnValidModrlAndView() {
        
        Product product = new Product(UUID.randomUUID(), "Product #", "Product #1 description");
        Mockito.doReturn(Optional.of(product)).when(this.productRepository).findOneById(ArgumentMatchers.any());

        ModelAndView modelAndView = this.productController.product(product.getId());

        Assertions.assertNotNull(modelAndView);
        Assertions.assertEquals(product, modelAndView.getModel().get("product"));
        Assertions.assertEquals("catalog/products/product", modelAndView.getViewName());
        Assertions.assertEquals(HttpStatus.OK, modelAndView.getStatus());

        Mockito.verify(this.productRepository).findOneById(ArgumentMatchers.eq(product.getId()));
    }

    @Test
    public void product_ProductDoesNotExist_ReturnModelAndViewWithError() throws Exception {

        UUID productId = UUID.randomUUID();
        Mockito.doReturn(Optional.empty()).when(this.productRepository).findOneById(ArgumentMatchers.any());

        ModelAndView modelAndView = this.productController.product(productId);

        Assertions.assertNotNull(modelAndView);
        Assertions.assertEquals("Couldn't find a product", modelAndView.getModel().get("error"));
        Assertions.assertEquals("errors/404", modelAndView.getViewName());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, modelAndView.getStatus());

        Mockito.verify(this.productRepository).findOneById(ArgumentMatchers.eq(productId));
    }

    @Test
    public void list_ReturnsValidModelAndView() {
        List<Product> products = IntStream.range(1, 4)
            .mapToObj(i -> new Product(UUID.randomUUID(), "Product #" + i, "Product #" + 1 + " description"))
            .collect(Collectors.toList());

        Mockito.doReturn(products).when(this.productRepository).findAll();
        
        ModelAndView modelAndView = this.productController.list();

        Assertions.assertNotNull(modelAndView);
        Assertions.assertEquals(products, modelAndView.getModel().get("products"));
        Assertions.assertEquals("catalog/products/list", modelAndView.getViewName());
        Assertions.assertEquals(HttpStatus.OK, modelAndView.getStatus());

        Mockito.verify(this.productRepository).findAll();
    } 

}
