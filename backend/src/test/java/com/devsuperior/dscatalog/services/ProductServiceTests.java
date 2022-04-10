package com.devsuperior.dscatalog.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception {
		 existingId = 1L;
		 nonExistingId = 100L;
		 countTotalProducts = 25L;
		 
		 doNothing().when(repository).deleteById(existingId);;
		 doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistingId);;
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
		
		verify(repository, times(1)).deleteById(nonExistingId);
	}
	
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		
		verify(repository, times(1)).deleteById(existingId);
	}
	
	
	

}
