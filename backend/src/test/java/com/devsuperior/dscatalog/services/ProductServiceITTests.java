package com.devsuperior.dscatalog.services;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@SpringBootTest
@Transactional
public class ProductServiceITTests {

	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository repositoryCategory;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private long countTotalProducts;
	private PageImpl<Product> page;
	private Product product;
	private ProductDTO productDto;
	private Category category;
	
	@BeforeEach
	void setUp() throws Exception {
		 existingId = 1L;
		 nonExistingId = 2508L;
		 countTotalProducts = 25L;
		 dependentId = 3L;
		 product = Factory.createProduct(existingId);
		 productDto = Factory.createProductDTO(existingId);
		 page = new PageImpl<>(List.of(product));
		 category = new Category(2L, "Eletronics");
	}
	
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		service.delete(existingId);
		Assertions.assertEquals(countTotalProducts - 1, repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenNonExistsId() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}
	
	@Test
	public void findAllPagedShouldReturnPageWhenPage0Size10() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
		PageRequest pageRequest = PageRequest.of(50, 10);
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortByName() {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
	}
	
	
	

}
