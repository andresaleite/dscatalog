package com.devsuperior.dscatalog.tests;

import java.time.Instant;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {

	
	public static Product createProduct() {
		Product product = new Product( 1L, "Phone", 800.0, "Good Phone", "https://img.com/img.png", Instant.parse("2022-10-20T03:00:00Z"));
		product.getCategories().add(new Category(2L, "Electronics"));
		return product;
	}

	public static ProductDTO createProductDTO() {
		return new ProductDTO(createProduct(), createProduct().getCategories());
	}
}
