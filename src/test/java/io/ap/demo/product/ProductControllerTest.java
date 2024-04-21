package io.ap.demo.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

	@LocalServerPort
	private int port;

	private String base;
	
	@MockBean
	private ProductService productServiceMock;

	@Autowired
	private TestRestTemplate template;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.base = "http://localhost:" + port ;
	}

	@Test
	void when_getProduct_thenOk() {
		//given
		Product product = new Product("1", "PAMP Fortuna 1 Gram Gold Bar",
				"PAMP Fortuna 1 Gram Gold Bar in sealed assay");
		Mockito.when(productServiceMock.getProduct(product.getSku())).thenReturn(product);
		
		//when
		ResponseEntity<Product> response = template.getForEntity(base + "/products/" + product.getSku(), Product.class);
		Product actual = response.getBody();
		HttpStatusCode statusCode = response.getStatusCode();

		//then
		assertTrue(statusCode.is2xxSuccessful());
		assertThat(actual).isEqualTo(product);
		
	}
}
