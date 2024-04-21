package io.ap.demo.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepositoryMock;

	
	@Test
	void should_createNewProduct() {

		// given
		Product product = new Product("1", "PAMP Fortuna 1 Gram Gold Bar",
				"PAMP Fortuna 1 Gram Gold Bar in sealed assay");
		when(productRepositoryMock.save(product)).thenReturn(product);

		// service method call
		productService.addProduct(product);

		// then no exception thrown
	}

	@Test
	void should_GetSingleProduct() {

		// given
		Product expectedProduct = new Product("1", "PAMP Fortuna 1 Gram Gold Bar",
				"PAMP Fortuna 1 Gram Gold Bar in sealed assay");
		when(productRepositoryMock.findById(any())).thenReturn(Optional.of(expectedProduct));
		String sku = "1";

		Product product = productService.getProduct(sku);

		assertThat(product).isEqualTo(expectedProduct);
	}

	@Test
	void should_RetrieveAllProducts() {

		// given
		List<Product> expectedProducts = new ArrayList<>(Arrays.asList(
				new Product("1", "PAMP Fortuna 1 Gram Gold Bar", "PAMP Fortuna 1 Gram Gold Bar in sealed assay"),
				new Product("2", "Valcambi 1 Gram Gold Bar", "Valcambi 1 Gram Gold Bar in sealed assay"),
				new Product("3", "PAMP Fortuna 1 Oz Gold Bar", "PAMP Fortuna 1 Oz Gold Bar in sealed assay")));

		when(productRepositoryMock.findAll()).thenReturn(expectedProducts);

		List<Product> products = productService.getAllProducts();

		assertThat(expectedProducts).isEqualTo(products);

	}

	@Test
	void should_ExceptionNotThrown_when_UpdateProduct() {
		// given
		Product product = new Product("1", "PAMP Fortuna 1 Gram Gold Bar",
				"PAMP Fortuna 1 Gram Gold Bar in sealed assay");

		when(productRepositoryMock.save(product)).thenReturn(product);
		
		productService.updateProduct(product.getSku(), product);

	}

	@Test
	void should_ExceptionNotThrown_when_DeleteProduct() {
		// given
		Product product = new Product("1", "PAMP Fortuna 1 Gram Gold Bar",
				"PAMP Fortuna 1 Gram Gold Bar in sealed assay");

		doNothing().when(productRepositoryMock).deleteById(product.getSku());
		productService.deleteProduct(product.getSku());
	}

}
