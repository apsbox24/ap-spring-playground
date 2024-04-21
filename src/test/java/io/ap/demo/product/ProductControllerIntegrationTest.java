package io.ap.demo.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.text.IsEmptyString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerIntegrationTest {

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@MockBean
	private ProductService productServiceMock;

	@Test
	void when_getProduct_thenOk() throws Exception {

		// given
		Product product = new Product("1", "PAMP Fortuna 1 Gram Gold Bar",
				"PAMP Fortuna 1 Gram Gold Bar in sealed assay");

		Mockito.when(productServiceMock.getProduct(any())).thenReturn(product);

		// then
		Product actualProduct = RestAssured
		.given()
			.pathParam("sku", product.getSku())
		.when()
			.get("/products/{sku}")
		.then()
			.statusCode(equalTo(HttpStatus.OK.value()))
			.extract().as(Product.class);
		
		assertThat(actualProduct).isEqualTo(product);
	}
	
	@Test
	void when_getAllProducts_thenOk() throws Exception {

		// given
		List<Product> products = new ArrayList<> (Arrays.asList(
				new Product("1", "PAMP Fortuna 1 Gram Gold Bar", "PAMP Fortuna 1 Gram Gold Bar in sealed assay"),
				new Product("2", "Valcambi 1 Gram Gold Bar", "Valcambi 1 Gram Gold Bar in sealed assay")
		));
		
		Mockito.when(productServiceMock.getAllProducts()).thenReturn(products);

		// then
		Product[] respList = RestAssured
		.when()
			.get("/products")
		.then()
			.statusCode(equalTo(HttpStatus.OK.value()))
			.log().all()
			.extract().as(Product[].class);
		
		assertThat(respList.length).isEqualTo(products.size());
		assertThat(respList).containsAll(products);
	}

	@Test
	public void when_createProduct_thenOk() throws Exception {
		Product product = new Product("1", "PAMP Fortuna 1 Gram Gold Bar",
				"PAMP Fortuna 1 Gram Gold Bar in sealed assay");
		Mockito.when(productServiceMock.addProduct(any())).thenReturn(product);

		RestAssured
		.given()
			.contentType(ContentType.JSON)
			.body(product)
		.when()
			.post("/products")
		.then()
			.statusCode(201)
			.assertThat().body("sku", equalTo(product.getSku())).assertThat()
			.body("name", equalTo(product.getName()));
	}

	@Test
	public void when_updateProduct_thenOk() throws Exception {
		Product product = new Product("1", "PAMP Fortuna 1 Gram Gold Bar",
				"PAMP Fortuna 1 Gram Gold Bar in sealed assay");

		Mockito.when(productServiceMock.updateProduct(any(), any())).thenReturn(product);

		RestAssured
		.given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(product)
		.when()
			.put("/products/" + product.getSku())
		.then()
			.statusCode(200)
			.body("sku", equalTo(product.getSku()))
			.body("name", equalTo(product.getName()));
	}

	@Test
	public void when_deleteProduct_thenOk() throws Exception {
		  Product product = new Product("1", "PAMP Fortuna 1 Gram Gold Bar",
		  "PAMP Fortuna 1 Gram Gold Bar in sealed assay");
		  doNothing().when(productServiceMock).deleteProduct(any());
		  
		  RestAssured
			.when()
				.delete("/products/" + product.getSku())
			.then()
				.statusCode(HttpStatus.ACCEPTED.value())
				.body(IsEmptyString.emptyOrNullString());
		
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
