package com.devsuperior.dscatalog.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository repositoryCategory;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private ProductDTO productDto;
	private Category category;
	
	@BeforeEach
	void setUp() throws Exception {
		 existingId = 1L;
		 nonExistingId = 2508L;
		 dependentId = 3L;
		 product = Factory.createProduct(existingId);
		 productDto = Factory.createProductDTO(existingId);
		 page = new PageImpl<>(List.of(product));
		 category = new Category(2L, "Eletronics");
		 
		 when(repository.getById(existingId)).thenReturn(product);
		 when(repositoryCategory.getById(existingId)).thenReturn(category);
		 doThrow(ResourceNotFoundException.class).when(repository).getById(nonExistingId);
		 doThrow(ResourceNotFoundException.class).when(repositoryCategory).getById(nonExistingId);
		 
		 when(repository.findById(existingId)).thenReturn(Optional.of(product));
		 when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		 when(repository.save(product)).thenReturn(product);
		 when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		 
		 doNothing().when(repository).deleteById(existingId);;
		 doThrow(EntityNotFoundException.class).when(repository).deleteById(nonExistingId);
		 doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		 doThrow(ResourceNotFoundException.class).when(repository).findById(nonExistingId);
		 doThrow(ResourceNotFoundException.class).when(repository).save( Factory.createProduct(nonExistingId));
	}
	
	
	@Test
	public void updateShouldReturnThrowResourceNotFoundExceptionWhenIdDoesNotExisting() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId,productDto);
		});
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenExistingId() {
		Assertions.assertNotNull(service.update(existingId,productDto));
		Assertions.assertEquals(Factory.createProductDTO(existingId).getId(), service.update(existingId,productDto).getId());
		Assertions.assertEquals(Factory.createProductDTO(existingId).getPrice(), service.update(existingId,productDto).getPrice());
		Assertions.assertEquals(Factory.createProductDTO(existingId).getDate(), service.update(existingId,productDto).getDate());
		Assertions.assertEquals(Factory.createProductDTO(existingId).getDescription(), service.update(existingId,productDto).getDescription());
		Assertions.assertEquals(Factory.createProductDTO(existingId).getImgUrl(), service.update(existingId,productDto).getImgUrl());
		Assertions.assertEquals(Factory.createProductDTO(existingId).getName(), service.update(existingId,productDto).getName());
		Assertions.assertEquals(2, service.update(existingId,productDto).getCategories().get(0).getId());
		
	}
	
	@Test
	public void findByIdShouldReturnThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		
		
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO	result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
		
		
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageable);
		Assertions.assertNotNull(result);
		verify(repository).findAll(pageable);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependId() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
		
		verify(repository, times(1)).deleteById(dependentId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
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
