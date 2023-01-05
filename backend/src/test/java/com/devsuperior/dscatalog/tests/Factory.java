package com.devsuperior.dscatalog.tests;

import java.time.Instant;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {

	
	public static Product createProduct(long id) {
		Product product = new Product( id, "Phone", 800.0, "Good Phone", "https://img.com/img.png", Instant.parse("2022-10-20T03:00:00Z"));
		product.getCategories().add(createCategory(id));
		return product;
	}
	
	public static ProductDTO createProductDTO(long id) {
		return new ProductDTO(createProduct(id), createProduct(id).getCategories());
	}
	
	public static ProductDTO createProductDTO() {
		return new ProductDTO();
	}

	public static Category createCategory(long id) {
		return new Category(id, "Electronics");
	}
}
