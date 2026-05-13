package com.jspider.spring_boot_simple_crud_with_mysql.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jspider.spring_boot_simple_crud_with_mysql.dao.ProductDao;
import com.jspider.spring_boot_simple_crud_with_mysql.entity.Product;
import com.jspider.spring_boot_simple_crud_with_mysql.responses.ProductPageResponse;
import com.jspider.spring_boot_simple_crud_with_mysql.responses.ResponseStructure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/product")
@CrossOrigin(value = "")
@Tag(name = "productcontroller", description = "this is controller class")
public class ProductController {
	// Rule Applied

	@Autowired
	ProductDao productDao;

	@Autowired
	ResponseStructure<Product> responseStructure;

	@GetMapping(value = "/getTodayDate")
	public String getTodaysDate() {

		return LocalDate.now() + " ";
	}

	@PostMapping(value = "/saveProduct")
	@Operation(description = "it will save one object at a time",
	responses = {
			@ApiResponse(responseCode = "200", description = "Product saved successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input, object not saved"),
			@ApiResponse(responseCode = "406", description = "Not acceptable, validation failed"),
			@ApiResponse(responseCode = "500", description = "Internal server error") }

	)
	public ResponseStructure<Product> saveProductController(@RequestBody Product product) {

		System.out.println(product);

		Product product2 = productDao.saveProductDao(product);

		if (product2 != null) {
			responseStructure.setStatusCode(HttpStatus.OK.value());
			responseStructure.setApiDescription("save product Secessfully...");
			responseStructure.setData(product2);
			return responseStructure;
		} else {

			responseStructure.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
			responseStructure.setApiDescription("data not saved something went wrong");
			responseStructure.setData(product2);
			return responseStructure;
		}

	}

	@PostMapping(value = "/saveProducts")
	public List<Product> saveProductController(@RequestBody List<Product> products) {

		System.out.println(products);
		return productDao.saveMultipleProductDao(products);
	}

	@GetMapping(value = "/findAllProduct")
	public List<Product> findAllProductController() {

		return productDao.displayAllProductDao();
	}

	@GetMapping(value = "/getProduct/{id}")
	public Product getProductByIdController(@PathVariable(name = "id") Integer id) {

		return productDao.getProductByIdDao(id);
	}

	@GetMapping(value = "/getProductByName/{name}")
	public List<Product> getProductByNameDao(@PathVariable(name = "name") String name) {
		return productDao.getProductByNameDao(name);
	}

	@GetMapping(value = "/getProductByPrice/{price}")
	public List<Product> getProductByPriceController(@PathVariable(name = "price") double price) {
		return productDao.getProductByPriceDao(price);
	}

	@DeleteMapping(value = "/deleteProductByPrice/{price}")
	public void deleteProductByPriceController(@PathVariable(name = "price") double price) {

		productDao.deleteProductByPriceDao(price);
	}
	
	//update
	
	

	@PutMapping(value = "/updateProduct/{id}")
	@Operation(description = "it will update one object at a time",
	responses = {
			@ApiResponse(responseCode = "200", description = "Product Update successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input, object not saved"),
			@ApiResponse(responseCode = "406", description = "Not acceptable, validation failed"),
			@ApiResponse(responseCode = "500", description = "Internal server error") }

	)
	public ResponseStructure<Product> updateProductController(@RequestBody Product userproduct, @PathVariable(name = "id") Integer id) {
        
		 

		Product product2 = productDao.updateProductDao(userproduct, id);

		if (product2 != null) {
			responseStructure.setStatusCode(HttpStatus.OK.value());
			responseStructure.setApiDescription("update product Secessfully...");
			responseStructure.setData(product2);
			return responseStructure;
		} else {

			responseStructure.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
			responseStructure.setApiDescription("data not saved something went wrong");
			responseStructure.setData(product2);
			return responseStructure;
		}

	}

	
	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable Integer id) {
	    try {
	        Product updatedProduct = productDao.updateProductDao(product, id);
	        return new ResponseEntity<Product>(updatedProduct, HttpStatus.OK);
	    } catch (RuntimeException e) {
	        return new ResponseEntity<Product>(HttpStatus.NOT_FOUND); // Make sure this line ends with ;
	    }
	}

	/**
	 * Paginated, sortable, and optionally name-filtered listing of products.
	 *
	 * <p>Resolved URL: {@code GET /product/products} — composed from the
	 * class-level {@code @RequestMapping("/product")} and the method-level
	 * {@code @GetMapping("/products")}. The class-level mapping is preserved
	 * verbatim so the 10 pre-existing endpoints continue to work unchanged.
	 *
	 * <p>Query parameter contract (all camelCase per Rule 1):
	 * <ul>
	 *   <li>{@code page}      — zero-based page index; defaults to {@code 0}</li>
	 *   <li>{@code size}      — page size; defaults to {@code 5}</li>
	 *   <li>{@code sortBy}    — name of the {@link Product} field to sort by;
	 *                            defaults to {@code "id"}</li>
	 *   <li>{@code direction} — sort direction "asc" or "desc"; defaults to
	 *                            {@code "asc"}</li>
	 *   <li>{@code name}      — optional case-insensitive substring filter
	 *                            against the product name column; omitted /
	 *                            null / blank disables filtering</li>
	 * </ul>
	 *
	 * <p>Delegates to {@link ProductDao#getProductsPagedDao(int, int, String,
	 * String, String)} which builds the {@link org.springframework.data.domain.Sort}
	 * + {@link org.springframework.data.domain.Pageable}, selects the filtered
	 * vs. unfiltered repository call, and returns a {@link Page} of
	 * {@link Product}. This method then assembles the {@link ProductPageResponse}
	 * envelope from the page metadata.
	 *
	 * @param page      zero-based page index (default "0")
	 * @param size      page size (default "5")
	 * @param sortBy    field name to sort by (default "id")
	 * @param direction "asc" or "desc" (default "asc")
	 * @param name      optional substring filter; {@code null} or blank means
	 *                  no filtering
	 * @return a {@link ProductPageResponse} containing the page rows and the
	 *         pagination metadata ({@code content}, {@code currentPage},
	 *         {@code totalItems}, {@code totalPages})
	 */
	@GetMapping("/products")
	public ProductPageResponse getProductsPagedController(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction,
			@RequestParam(name = "name", required = false) String name) {

		// Rule 3: at least one log statement per new method.
		System.out.println("getProductsPagedController called with page=" + page + ", size=" + size
				+ ", sortBy=" + sortBy + ", direction=" + direction + ", name=" + name);

		// Local variable name 'productPage' avoids collision with the 'page' parameter.
		Page<Product> productPage = productDao.getProductsPagedDao(page, size, sortBy, direction, name);

		// ProductPageResponse is a plain DTO (not a Spring bean) — instantiated per call.
		ProductPageResponse productPageResponse = new ProductPageResponse();
		productPageResponse.setContent(productPage.getContent());
		productPageResponse.setCurrentPage(productPage.getNumber());
		productPageResponse.setTotalItems(productPage.getTotalElements());
		productPageResponse.setTotalPages(productPage.getTotalPages());

		return productPageResponse;
	}

}
