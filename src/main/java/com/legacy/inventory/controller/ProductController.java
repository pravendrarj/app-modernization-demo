package com.legacy.inventory.controller;

import com.legacy.inventory.model.Product;
import com.legacy.inventory.service.NotificationService;
import com.legacy.inventory.service.ProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * OUTDATED PATTERNS:
 * - Uses javax.servlet (should be jakarta.servlet)
 * - Uses @RequestMapping instead of specific @GetMapping, @PostMapping etc.
 * - Manual JSON/response construction
 * - Direct HttpServletRequest/Response usage
 * - No proper error handling (ResponseEntityExceptionHandler)
 * - No API versioning
 * - Log4j 1.x
 * - SimpleDateFormat (thread-unsafe)
 * - Returns raw HashMap instead of proper DTOs
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = Logger.getLogger(ProductController.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private ProductService productService;

    @Autowired
    private NotificationService notificationService;

    // OUTDATED: Using @RequestMapping instead of @GetMapping
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            // OUTDATED: Wrapping in HashMap instead of proper response object
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", products);
            response.put("count", products.size());
            response.put("timestamp", new Date().toString());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching products", e);
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Product not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            Product created = productService.createProduct(product);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating product", e);
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product updated = productService.updateProduct(id, product);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // OUTDATED: Using HttpServletRequest directly for query params
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<?> searchProducts(HttpServletRequest request) {
        String searchTerm = request.getParameter("q");
        if (searchTerm == null || searchTerm.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Search term is required");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        // WARNING: searchTerm passed directly to SQL - injection risk
        List<Product> results = productService.searchProducts(searchTerm);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/category/{category}", method = RequestMethod.GET)
    public ResponseEntity<?> getByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @RequestMapping(value = "/low-stock", method = RequestMethod.GET)
    public ResponseEntity<?> getLowStockProducts(@RequestParam(defaultValue = "10") int threshold) {
        List<Product> products = productService.getLowStockProducts(threshold);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // OUTDATED: Manual date parsing from string
    @RequestMapping(value = "/date-range", method = RequestMethod.GET)
    public ResponseEntity<?> getByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            List<Product> products = productService.getProductsByDateRange(start, end);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (ParseException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid date format. Use yyyy-MM-dd");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public ResponseEntity<?> getInventorySummary() {
        Map<String, Integer> summary = productService.getInventorySummary();
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }

    // OUTDATED: Using HttpServletResponse directly for file download
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportProducts(HttpServletResponse response) throws IOException {
        List<Product> products = productService.getAllProducts();

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=products.csv");

        // OUTDATED: Writing directly to response output stream
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Name,SKU,Price,Quantity,Category\n");
        for (Product p : products) {
            csv.append(p.getId()).append(",")
               .append(p.getProductName()).append(",")
               .append(p.getSku()).append(",")
               .append(p.getPrice()).append(",")
               .append(p.getQuantity()).append(",")
               .append(p.getCategory()).append("\n");
        }

        response.getWriter().write(csv.toString());
        response.getWriter().flush();
    }

    // OUTDATED: Update stock via POST with query parameters
    @RequestMapping(value = "/{id}/stock", method = RequestMethod.POST)
    public ResponseEntity<?> updateStock(
            @PathVariable Long id,
            @RequestParam int quantity) {
        try {
            productService.updateStock(id, quantity);
            Map<String, String> result = new HashMap<>();
            result.put("message", "Stock updated");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
