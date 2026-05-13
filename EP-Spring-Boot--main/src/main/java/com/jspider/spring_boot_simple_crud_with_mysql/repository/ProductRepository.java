package com.jspider.spring_boot_simple_crud_with_mysql.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.jspider.spring_boot_simple_crud_with_mysql.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	// Rule Applied

	List<Product> findByName(String name);

	/**
	 * Derived Spring Data JPA finder that returns a page of products whose
	 * {@code name} contains the supplied substring in a case-insensitive manner.
	 *
	 * <p>Spring Data parses the method name and synthesizes SQL of the form
	 * {@code WHERE LOWER(name) LIKE LOWER(?)} with the parameter wrapped as
	 * {@code %name%}. The supplied {@link Pageable} carries page index, page
	 * size, and sort information; the returned {@link Page} carries the matching
	 * rows along with the metadata (total elements, total pages, page number)
	 * needed by the {@code GET /product/products} endpoint to build its
	 * paginated response envelope.</p>
	 *
	 * <p>This method supports the new pagination/sorting/filtering feature and
	 * does not affect the existing exact-match {@link #findByName(String)}
	 * derived query, which continues to back the legacy
	 * {@code /product/getProductByName/{name}} endpoint.</p>
	 *
	 * @param name     the substring to match against the product {@code name}
	 *                 column (case-insensitive); must not be {@code null}
	 * @param pageable the pagination and sort information; must not be
	 *                 {@code null}
	 * @return a {@link Page} of matching {@link Product} rows for the requested
	 *         page slice
	 */
	Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

	@Query(value = "select * from product where price=?",nativeQuery = true)
	List<Product> getProductByPrice(double price);
	
	@Query(value = "delete from product where price=?",nativeQuery = true)
	@Modifying
	@Transactional
	void deleteProductByPrice(double price);
}
