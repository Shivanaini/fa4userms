package com.infosys.infymarket.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.infosys.infymarket.user.dto.BuyerDTO;
import com.infosys.infymarket.user.dto.CartDTO;
import com.infosys.infymarket.user.dto.LoginDTO;
import com.infosys.infymarket.user.dto.ProductDTO;
import com.infosys.infymarket.user.dto.SellerDTO;
import com.infosys.infymarket.user.dto.WishlistDTO;
import com.infosys.infymarket.user.entity.Buyer;
import com.infosys.infymarket.user.exception.InfyMarketException;
import com.infosys.infymarket.user.service.BuyerService;
import com.infosys.infymarket.user.service.SellerService;

@RestController

@RequestMapping
public class BuyerController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	Environment environment;
	@Value("${product.uri}")
	String product;

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	BuyerService buyerservice;
//	@Autowired
//	SellerService sellerservice;

	//REGISTERING BUYER-->POST
	@PostMapping(value = "/api/buyer/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createBuyer(@Valid @RequestBody BuyerDTO buyerDTO) throws InfyMarketException {
		
			String successMessage = environment.getProperty("API.INSERT_SUCCESS");
		//	logger.info("Registration request for buyer with data {}", buyerDTO);
			buyerservice.saveBuyer(buyerDTO);
			return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
	}

	//Get all buyers
	@GetMapping(value = "/api/buyers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<BuyerDTO>> getAllBuyer() throws InfyMarketException {
		
			List<BuyerDTO> buyerDTOs = buyerservice.getAllBuyer();
			return new ResponseEntity<>(buyerDTOs, HttpStatus.OK);
		
	}

	//GET ALL BUYERS-->GET
	@GetMapping(value = "/buyer/{buyerid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BuyerDTO> getBuyerById(@PathVariable String buyerid) throws InfyMarketException {
		
			BuyerDTO buyer = buyerservice.getBuyerById(buyerid);
			return new ResponseEntity<>(buyer, HttpStatus.OK);
		
	}

	//LOGIN BUYER-->POST
	@PostMapping(value = "/buyer/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO) throws InfyMarketException {
		
			buyerservice.login(loginDTO);
			String successMessage = environment.getProperty("API.LOGIN_SUCCESS");
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		
	}

	//DELETE BUYER-->DELETE
	@DeleteMapping(value = "/buyer/{buyerid}")
	public ResponseEntity<String> deleteBuyer(@PathVariable String buyerid) throws InfyMarketException {
		
			buyerservice.deleteBuyer(buyerid);
			String successMessage = environment.getProperty("API.DELETE_SUCCESS");
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		
	}

	//ADDING WISHLIST-->POST
	@PostMapping(value = "/api/wishlist/add", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveWishlist(@RequestBody WishlistDTO wishlistDTO) throws InfyMarketException {
		
			logger.info("Creation request for customer {} with data {}", wishlistDTO);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("prodid", wishlistDTO.getProdid().getProdid());
			// map.put("buyerid",wishlistDTO.getBuyerid());
			System.out.println("adding map" + map);
			ProductDTO projectDTOs = restTemplate.getForObject(product,
					ProductDTO.class, map);
			System.out.println("adding wishlist" + projectDTOs);
			buyerservice.createWishlist(wishlistDTO);
			String successMessage = environment.getProperty("API.WISHLIST_SUCCESS");
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		

	}

	//DELETE WISHLIST-->DELETE
	@DeleteMapping(value = "/wishlist/{buyerid}")
	public ResponseEntity<String> deleteWishlist(@PathVariable String buyerid) throws Exception {
		
			buyerservice.deleteWishlist(buyerid);
			String successMessage = environment.getProperty("API.DELETE_SUCCESS");
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		

	}

	//ADDING CART -->POST
	@PostMapping(value = "/api/cart/add", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveCart(@RequestBody CartDTO cartDTO) throws InfyMarketException {
		
		//	logger.info("Creation request for customer {} with data {}", cartDTO);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("buyerid", cartDTO.getBuyerid().getBuyerid());
			map.put("prodid", cartDTO.getProdid().getProdid());
			// map.put("buyerid",wishlistDTO.getBuyerid());
			System.out.println("adding map" + map);
			ProductDTO projectDTOs = restTemplate.getForObject(product,
					ProductDTO.class, map);
			System.out.println("adding cart" + projectDTOs);
			buyerservice.createCart(cartDTO);
			String successMessage = environment.getProperty("API.CART_SUCCESS");
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		

	}

	//DELETE CART-->DELETE
	@DeleteMapping(value = "/api/cart/{buyerid}")
	public ResponseEntity<String> deleteCart(@PathVariable String buyerid) throws Exception {
		
			buyerservice.deleteCart(buyerid);
			String successMessage = environment.getProperty("API.DELETE_SUCCESS");
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		

	}

	//UPDATE ISPRIVILEGED-->PUT
	@RequestMapping(value = "/api/isprivilege/{buyerid}", method = RequestMethod.PUT)
	public ResponseEntity<Buyer> updateIsprivilege(@RequestBody Buyer buyer, @PathVariable String buyerid)
			throws InfyMarketException {
		
			Buyer buyers = buyerservice.updateIsprivilege(buyer, buyerid);
			return new ResponseEntity<>(buyers, HttpStatus.OK);
		

	}
	
}
