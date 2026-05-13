package com.jspider.spring_boot_simple_crud_with_mysql.responses;

import java.util.List;

import com.jspider.spring_boot_simple_crud_with_mysql.entity.Product;

import lombok.Data;

@Data
public class ProductPageResponse {

	// Rule Applied
	private List<Product> content;
	private int currentPage;
	private long totalItems;
	private int totalPages;
}
