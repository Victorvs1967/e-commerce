package com.vvs.ecommerce.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.vvs.ecommerce.entity.Product;
import com.vvs.ecommerce.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductsControllerIntegrationTest {
    
    @Autowired
    MockMvc mockMvc;

    @SpyBean
    ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        this.productRepository.getProducts().clear();
        List<Product> products = IntStream.range(1, 4)
            .mapToObj(i -> new Product(UUID.randomUUID(), "Product #" + i, "Product #" + i + " description"))
            .collect(Collectors.toList());
        this.productRepository.getProducts().addAll(products);
    }

    @Test
    public void product_ProductExist_ReturnsProductPage() throws Exception {
        UUID id = this.productRepository.getProducts().get(1).getId();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/catalog/products/{productId}", id))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.model().attribute("product", this.productRepository.getProducts().get(1)))
            .andExpect(MockMvcResultMatchers.view().name("catalog/products/product"))
            .andExpect(MockMvcResultMatchers.xpath("//meta[@name='product-id']/@content").string(id.toString()));
        Mockito.verify(this.productRepository).findOneById(ArgumentMatchers.eq(id));
    }

    @Test
    public void product_ProductDoesNotExist_ReturnErrorPage() throws Exception {

        UUID id = UUID.randomUUID();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/catalog/products/{productId}", id))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.model().attribute("error", "Couldn't find a product"))
            .andExpect(MockMvcResultMatchers.view().name("errors/404"))
            .andExpect(MockMvcResultMatchers.xpath("//div[@class='error']").string("Couldn't find a product"));
        Mockito.verify(this.productRepository).findOneById(ArgumentMatchers.eq(id));
    }

    @Test
    public void list_ReturnsProductsListPage() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/catalog/products/list"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.model().attribute("products", this.productRepository.getProducts()))
            .andExpect(MockMvcResultMatchers.view().name("catalog/products/list"))
            .andExpect(MockMvcResultMatchers.xpath("//li[@class='product list-group-item']").nodeCount(3));

        Mockito.verify(this.productRepository).findAll();
    }
}
