package com.infosys.infymarket.user.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.infosys.infymarket.user.dto.LoginDTO;
import com.infosys.infymarket.user.dto.ProductDTO;
import com.infosys.infymarket.user.dto.SellerDTO;
import com.infosys.infymarket.user.exception.InfyMarketException;
import com.infosys.infymarket.user.service.SellerService;

@RestController

@RequestMapping
public class SellerController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	Environment environment;
	@Value("${product.uri}")
	String product;

	@Value("${productsell.uri}")
	String prod;
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	SellerService sellerservice;

	// Seller register
	@PostMapping(value = "/api/seller/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createSeller(@Valid @RequestBody SellerDTO sellerDTO) throws InfyMarketException {
		
			logger.info("Registration request for seller with data {}", sellerDTO);
			sellerservice.saveSeller(sellerDTO);
			String successMessage = environment.getProperty("API.INSERT_SUCCESS");
			return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
		
	}

	// Get all sellers
	@GetMapping(value = "/api/sellers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SellerDTO>> getAllSeller() throws InfyMarketException {
		
			List<SellerDTO> sellerDTOs = sellerservice.getAllSeller();
			return new ResponseEntity<>(sellerDTOs, HttpStatus.OK);
		
	}

	// Seller login
	@PostMapping(value = "/seller/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> Login(@Valid @RequestBody LoginDTO loginDTO) throws InfyMarketException {
	
			sellerservice.login(loginDTO);
		//	logger.info("Login request for seller {} with password {}", loginDTO.getEmail(), loginDTO.getPassword());
			String successMessage = environment.getProperty("API.LOGIN_SUCCESS");
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		
	}

	// Delete seller
	@DeleteMapping(value = "/seller/{sellerid}")
	public ResponseEntity<String> deleteSeller(@PathVariable String sellerid) throws InfyMarketException {
		
			sellerservice.deleteSeller(sellerid);
			String successMessage = environment.getProperty("API.DELETE_SUCCESS");
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		
	}

	// Get products of seller
	@GetMapping(value = "/api/sellers/products/{sellerid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductDTO>> getProductsOfSeller(@PathVariable String sellerid)
			throws InfyMarketException {
		
			@SuppressWarnings("unchecked")
			List<ProductDTO> productDTO = new RestTemplate()
					.getForObject(prod + sellerid, List.class);
			return new ResponseEntity<>(productDTO, HttpStatus.OK);
		
	}
}
