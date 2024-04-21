package io.ap.demo.product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

	private ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/products")
	public List<Product> getAllProducts() {
		return productService.getAllProducts();
	}
	
	@GetMapping("/products/{sku}")
	public Product getProduct(@PathVariable String sku) {
		return productService.getProduct(sku);
	}
	
	@PostMapping ("/products")
	public ResponseEntity<Product> addProduct(@RequestBody Product product) {
		return new ResponseEntity<Product>(productService.addProduct(product), HttpStatus.CREATED);
	}
	
	@PutMapping ("/products/{sku}")
	public ResponseEntity<Product> updateProduct(@PathVariable String sku, @RequestBody Product product) {
		return new ResponseEntity<Product>(productService.updateProduct(sku, product), HttpStatus.OK);
	}
	
	@DeleteMapping("/products/{sku}")
	public ResponseEntity<HttpStatus> deleteProduct(@PathVariable String sku) {
		productService.deleteProduct(sku);
	    return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
	}

}
