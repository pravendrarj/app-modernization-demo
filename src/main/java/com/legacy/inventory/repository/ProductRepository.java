package com.legacy.inventory.repository;

import com.legacy.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * OUTDATED: Uses Spring Data JPA but with native queries and old patterns
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    // OUTDATED: Using native query instead of derived query
    @Query(value = "SELECT * FROM products WHERE category = :category", nativeQuery = true)
    List<Product> findByCategory(@Param("category") String category);

    // OUTDATED: Using java.util.Date parameter
    @Query("SELECT p FROM Product p WHERE p.createdDate BETWEEN :startDate AND :endDate")
    List<Product> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT * FROM products WHERE is_active = 1", nativeQuery = true)
    List<Product> findActiveProducts();

    @Query(value = "SELECT * FROM products WHERE quantity < :threshold", nativeQuery = true)
    List<Product> findLowStockProducts(@Param("threshold") int threshold);

    List<Product> findBySupplier(String supplier);

    Product findBySku(String sku);
}
