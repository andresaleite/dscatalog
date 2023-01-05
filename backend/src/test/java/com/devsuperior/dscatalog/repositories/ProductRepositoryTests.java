package com.devsuperior.dscatalog.repositories;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception {
		 existingId = 1L;
		 nonExistingId = 100L;
		 countTotalProducts = 25L;
	}
	
	
	@Test
	public void saveShouldPersistWhithAutoincrementWhenIdIsNull() {
		Product product = Factory.createProduct(existingId);
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts+1, product.getId());
		
	}
	
	@Test
	public void deleteSholdDeleteObjectWhenIdExist() {
		repository.deleteById(existingId);
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteSholdThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});
	}
	
	@Test
	public void findByIdSholdReturnNonEmptyOptionalProductWhenIdExist() {
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
		Assertions.assertNotNull(result.get());
	}

	@Test
	public void findByIdSholdReturnEmptyOptionalProductWhenIdNotExist() {
		Optional<Product> result = repository.findById(nonExistingId);
		Assertions.assertTrue(result.isEmpty());
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			result.get();
		});
	}
	

	
}
