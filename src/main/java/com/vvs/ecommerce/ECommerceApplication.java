package com.vvs.ecommerce;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.vvs.ecommerce.entity.Product;
import com.vvs.ecommerce.repository.ProductRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ECommerceApplication {

	@Bean
	public CommandLineRunner commandLineRunner(ProductRepository productRepository) {
		return args -> {
			List<Product> products = IntStream.range(1, 4)
				.mapToObj(i -> new Product(UUID.randomUUID(), "Product #" + i, "Product #" + i + " description"))
				.collect(Collectors.toList());
			productRepository.getProducts().addAll(products);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

}
