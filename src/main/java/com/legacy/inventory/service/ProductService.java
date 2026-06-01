package com.legacy.inventory.service;

import com.legacy.inventory.dao.ProductDao;
import com.legacy.inventory.model.Product;
import com.legacy.inventory.repository.ProductRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * OUTDATED PATTERNS:
 * - Uses Log4j 1.x (CVE-2019-17571, should use SLF4J/Logback or Log4j2)
 * - Uses commons-lang 2.x (should be commons-lang3)
 * - Uses java.util.Date and SimpleDateFormat (thread-unsafe)
 * - Field injection with @Autowired (should use constructor injection)
 * - Raw exception handling
 * - No Optional usage
 * - Mutable collections returned directly
 */
@Service
@Transactional
public class ProductService {

    // OUTDATED: Log4j 1.x logger
    private static final Logger logger = Logger.getLogger(ProductService.class);

    // OUTDATED: SimpleDateFormat is not thread-safe
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // OUTDATED: Field injection instead of constructor injection
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDao productDao;

    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll();
    }

    // OUTDATED: Returns null instead of Optional
    public Product getProductById(Long id) {
        logger.info("Fetching product with id: " + id);
        Product product = productRepository.findOne(id);
        if (product == null) {
            logger.warn("Product not found with id: " + id);
            return null;
        }
        return product;
    }

    public Product getProductBySku(String sku) {
        if (StringUtils.isEmpty(sku)) {
            return null;
        }
        return productRepository.findBySku(sku);
    }

    public Product createProduct(Product product) {
        logger.info("Creating product: " + product.getProductName());

        // OUTDATED: Using java.util.Date
        product.setCreatedDate(new Date());
        product.setIsActive(true);

        // OUTDATED: Manual SKU generation with string concatenation
        if (StringUtils.isEmpty(product.getSku())) {
            String sku = "SKU-" + System.currentTimeMillis() + "-" + new Random().nextInt(1000);
            product.setSku(sku);
        }

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findOne(id);
        if (product == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        product.setProductName(productDetails.getProductName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        product.setCategory(productDetails.getCategory());
        product.setSupplier(productDetails.getSupplier());
        product.setUpdatedDate(new Date());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findOne(id);
        if (product != null) {
            productRepository.delete(product);
            logger.info("Deleted product: " + id);
        }
    }

    // OUTDATED: Using raw SQL through DAO with SQL injection risks
    public List<Product> searchProducts(String searchTerm) {
        logger.info("Searching products with term: " + searchTerm);
        return productDao.searchProducts(searchTerm);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getLowStockProducts(int threshold) {
        return productRepository.findLowStockProducts(threshold);
    }

    // OUTDATED: Using java.util.Date for date range
    public List<Product> getProductsByDateRange(Date startDate, Date endDate) {
        logger.info("Fetching products from " + dateFormat.format(startDate) +
                     " to " + dateFormat.format(endDate));
        return productRepository.findByDateRange(startDate, endDate);
    }

    // OUTDATED: Updating stock with raw SQL
    public void updateStock(Long productId, int newQuantity) {
        productDao.updateStock(productId, newQuantity);
    }

    // OUTDATED: Returns mutable internal collection
    public Map<String, Integer> getInventorySummary() {
        Map<String, Integer> summary = new HashMap<>();
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            String category = product.getCategory();
            if (category != null) {
                // OUTDATED: Pre-Java 8 style, should use streams
                if (summary.containsKey(category)) {
                    summary.put(category, summary.get(category) + product.getQuantity());
                } else {
                    summary.put(category, product.getQuantity());
                }
            }
        }

        return summary;
    }

    // OUTDATED: Synchronization for thread safety instead of ConcurrentHashMap
    public synchronized void bulkUpdatePrices(String category, double percentageChange) {
        List<Product> products = productRepository.findByCategory(category);
        for (Product product : products) {
            double newPrice = product.getPrice() * (1 + percentageChange / 100);
            product.setPrice(newPrice);
            product.setUpdatedDate(new Date());
            productRepository.save(product);
        }
        logger.info("Updated prices for category: " + category);
    }
}
