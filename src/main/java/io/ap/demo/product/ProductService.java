package io.ap.demo.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
	private ProductRepository productRepository;
	
	public ProductService (ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	public Product addProduct(Product product) {
		return productRepository.save(product);
	}

	public Product getProduct(String id) {
		return productRepository.findById(id).get();
	}

	public List<Product> getAllProducts() {
		List<Product> productList = new ArrayList<>();
		productRepository.findAll().forEach(productList::add);		
		return productList;
	}

	public Product updateProduct(String sku , Product product) {
		return productRepository.save(product);
	}

	public void deleteProduct(String sku) {
		productRepository.deleteById(sku);
	}

	
}

