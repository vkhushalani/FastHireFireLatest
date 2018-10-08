package com.amrest.fastHire.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;

import com.amrest.fastHire.model.Country;
import com.amrest.fastHire.service.CountryService;


@RestController
@RequestMapping("/TableMaint")
public class CountryController {
	
	private static String successMessage = "SUCCESS";
	Logger logger = LoggerFactory.getLogger(CountryController.class);
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@GetMapping("/Country")
	public ResponseEntity<List<Country>> getAll(){
		List<Country> countries = countryService.findAll();
	     return ResponseEntity.ok().body(countries);
	}
	
	@GetMapping("/Country/{id}")
	 public ResponseEntity <Country> getById(@PathVariable("id") String id) {
		Country country = countryService.findById(id);
		return ResponseEntity.ok().body(country);
	   }
	@PostMapping(value = "/Country")
	public ResponseEntity<?> create(@RequestBody Country country)  {
		// run the statement 
		jdbcTemplate.execute("SET 'user_Name' = 'jmk'");
		
		country =  countryService.create(country);
		return ResponseEntity.ok().body(successMessage);
		
	}
	
	@PutMapping(value = "/Country")
	public ResponseEntity<?> update(@RequestBody Country country)  {
		country =  countryService.update(country);
		 return ResponseEntity.ok().body(successMessage);
		
	}


}
