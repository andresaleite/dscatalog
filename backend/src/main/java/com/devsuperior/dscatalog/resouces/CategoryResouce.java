package com.devsuperior.dscatalog.resouces;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.services.CategoryService;

//Recurso da entidade
@RestController
@RequestMapping(value = "/categories")
public class CategoryResouce {

	@Autowired
	private CategoryService service;
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll() {
		List<CategoryDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	/*
	@GetMapping
	public ResponseEntity<Category> findById(Long id) {
		Category category = service.findById(id);
		return ResponseEntity.ok().body(category);
	}*/
}
