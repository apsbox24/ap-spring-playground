package io.ap.demo.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
class ProductControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productServiceMock;

	@Test
	void when_getProduct_thenOk() throws Exception {

		// given
		Product product = new Product("1", "PAMP Fortuna 1 Gram Gold Bar",
				"PAMP Fortuna 1 Gram Gold Bar in sealed assay");

		when(productServiceMock.getProduct(any())).thenReturn(product);

		// then
		mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", 1).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.sku").value(product.getSku()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(product.getName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(product.getDescription()));

	}

	@Test
	public void when_createProduct_thenOk() throws Exception {
		Product product = new Product("1", "PAMP Fortuna 1 Gram Gold Bar",
				"PAMP Fortuna 1 Gram Gold Bar in sealed assay");
		when(productServiceMock.addProduct(any())).thenReturn(product);
		
		//then
		mockMvc.perform(MockMvcRequestBuilders.post("/products").content(asJsonString(product))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.content().json(asJsonString(product)));
	}
	
	@Test
	public void when_updateProduct_thenOk() throws Exception {
		Product product = new Product("1", "PAMP Fortuna 1 Gram Gold Bar",
				"PAMP Fortuna 1 Gram Gold Bar in sealed assay");
		when(productServiceMock.updateProduct(any(), any())).thenReturn(product);
		
		//then
		mockMvc.perform(MockMvcRequestBuilders.put("/products/{sku}", product.getSku()).content(asJsonString(product))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(asJsonString(product)));
	}

	@Test
	public void when_deleteProduct_thenOk() throws Exception 
	{
		Product product = new Product("1", "PAMP Fortuna 1 Gram Gold Bar",
				"PAMP Fortuna 1 Gram Gold Bar in sealed assay");
		doNothing().when(productServiceMock).deleteProduct(any());
		
		//then
		mockMvc.perform( MockMvcRequestBuilders.delete("/products/{sku}", product.getSku()) )
	        .andExpect(status().isAccepted());
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
