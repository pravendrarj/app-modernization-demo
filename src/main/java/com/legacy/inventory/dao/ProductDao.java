package com.legacy.inventory.dao;

import com.legacy.inventory.model.Product;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * OUTDATED PATTERNS:
 * - Uses raw JdbcTemplate with manual RowMapper (should use Spring Data JPA)
 * - String concatenation for SQL (SQL injection risk)
 * - Uses deprecated commons-lang (should be commons-lang3)
 * - Manual date handling with java.util.Date
 */
@Repository
public class ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // SECURITY RISK: SQL injection via string concatenation
    public List<Product> searchProducts(String searchTerm) {
        String sql = "SELECT * FROM products WHERE product_name LIKE '%" + searchTerm + "%' " +
                     "OR description LIKE '%" + searchTerm + "%'";
        return jdbcTemplate.query(sql, new ProductRowMapper());
    }

    // SECURITY RISK: SQL injection
    public List<Product> findByFilters(String category, String supplier, Double minPrice, Double maxPrice) {
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1");

        if (StringUtils.isNotEmpty(category)) {
            sql.append(" AND category = '").append(category).append("'");
        }
        if (StringUtils.isNotEmpty(supplier)) {
            sql.append(" AND supplier = '").append(supplier).append("'");
        }
        if (minPrice != null) {
            sql.append(" AND price >= ").append(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND price <= ").append(maxPrice);
        }

        return jdbcTemplate.query(sql.toString(), new ProductRowMapper());
    }

    // OUTDATED: Raw SQL update
    public void updateStock(Long productId, int newQuantity) {
        String sql = "UPDATE products SET quantity = " + newQuantity +
                     ", updated_date = NOW() WHERE id = " + productId;
        jdbcTemplate.update(sql);
    }

    // OUTDATED: Manual bulk insert
    public void bulkInsertProducts(List<Product> products) {
        for (Product product : products) {
            String sql = "INSERT INTO products (product_name, sku, price, quantity, created_date) " +
                         "VALUES ('" + product.getProductName() + "', '" + product.getSku() + "', " +
                         product.getPrice() + ", " + product.getQuantity() + ", NOW())";
            jdbcTemplate.update(sql);
        }
    }

    /**
     * OUTDATED: Manual RowMapper implementation
     */
    private class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setId(rs.getLong("id"));
            product.setProductName(rs.getString("product_name"));
            product.setDescription(rs.getString("description"));
            product.setSku(rs.getString("sku"));
            product.setPrice(rs.getDouble("price"));
            product.setQuantity(rs.getInt("quantity"));
            product.setCategory(rs.getString("category"));
            product.setSupplier(rs.getString("supplier"));
            product.setCreatedDate(rs.getTimestamp("created_date"));
            product.setUpdatedDate(rs.getTimestamp("updated_date"));
            return product;
        }
    }
}
