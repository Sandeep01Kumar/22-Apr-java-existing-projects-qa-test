package com.jspider.spring_boot_simple_crud_with_mysql.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jspider.spring_boot_simple_crud_with_mysql.entity.Product;
import com.jspider.spring_boot_simple_crud_with_mysql.repository.ProductRepository;

@Repository
public class ProductDao {
	// Rule Applied

	@Autowired
	ProductRepository productRepository;

	public Product saveProductDao(Product product) {
		return productRepository.save(product);
	}

	public List<Product> saveMultipleProductDao(List<Product> product) {
		return productRepository.saveAll(product);
	}

	public List<Product> displayAllProductDao() {
		return productRepository.findAll();
	}

	public Product getProductByIdDao(Integer id) {

		Optional<Product> optional = productRepository.findById(id);

		return optional.isPresent() ? optional.get() : null;

	}

	public List<Product> getProductByNameDao(String name) {
		
		return productRepository.findByName(name);

	}
	
	public List<Product> getProductByPriceDao(double price){
		return productRepository.getProductByPrice(price);
	}
	
	public void deleteProductByPriceDao(double price) {
		
		productRepository.deleteProductByPrice(price);
	}
	
	public Product updateProductDao(Product product, Integer id) {
	    Optional<Product> optional = productRepository.findById(id);

	    if (optional.isPresent()) {
	        Product existingProduct = optional.get();

	        // Do NOT update the ID; it should remain unchanged
	        existingProduct.setName(product.getName());
	        existingProduct.setColor(product.getColor());
	        existingProduct.setPrice(product.getPrice());

	        return productRepository.save(existingProduct);
	    } else {
	        // You can handle not found case as you wish
	        throw new RuntimeException("Product not found with ID: " + id);
	    }
	}

	/**
	 * Retrieves a page of {@link Product} rows for the
	 * {@code GET /product/products} endpoint, applying optional case-insensitive
	 * name filtering and the supplied sort + pagination parameters.
	 *
	 * <p>Behavior:
	 * <ul>
	 *   <li>A {@link Sort} is built from {@code sortBy} and {@code direction}
	 *       via {@link Sort.Direction#fromString(String)} — which performs
	 *       case-insensitive parsing of "asc"/"desc" and throws
	 *       {@link IllegalArgumentException} for unrecognized values.</li>
	 *   <li>A {@link Pageable} is built from {@code page}, {@code size}, and
	 *       the {@code Sort} above.</li>
	 *   <li>If {@code name} is {@code null} or blank, the unfiltered
	 *       {@code findAll(Pageable)} path is taken (inherited from
	 *       {@code JpaRepository}).</li>
	 *   <li>Otherwise, the filtered derived finder
	 *       {@code findByNameContainingIgnoreCase(String, Pageable)} is
	 *       invoked, which produces SQL of the form
	 *       {@code WHERE LOWER(name) LIKE LOWER('%name%')}.</li>
	 * </ul>
	 *
	 * @param page      zero-based page index (default 0 in the controller)
	 * @param size      page size (default 5 in the controller)
	 * @param sortBy    name of the {@code Product} field to sort by
	 *                  (default {@code "id"} in the controller)
	 * @param direction sort direction; "asc" or "desc"
	 *                  (default {@code "asc"} in the controller)
	 * @param name      optional case-insensitive substring filter against the
	 *                  {@code name} column; {@code null} or blank disables the
	 *                  filter
	 * @return a {@link Page} of {@link Product} rows for the requested slice
	 */
	public Page<Product> getProductsPagedDao(int page, int size, String sortBy, String direction, String name) {
		System.out.println("getProductsPagedDao called with page=" + page + ", size=" + size + ", sortBy=" + sortBy + ", direction=" + direction + ", name=" + name);
		Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
		Pageable pageable = PageRequest.of(page, size, sort);
		if (name != null && !name.isBlank()) {
			return productRepository.findByNameContainingIgnoreCase(name, pageable);
		} else {
			return productRepository.findAll(pageable);
		}
	}

}
