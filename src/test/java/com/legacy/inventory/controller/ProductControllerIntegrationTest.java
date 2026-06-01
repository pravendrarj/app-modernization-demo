package com.legacy.inventory.controller;

import com.legacy.inventory.model.Product;
import com.legacy.inventory.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * OUTDATED PATTERNS:
 * - JUnit 4 with @RunWith(SpringRunner.class)
 * - Uses TestRestTemplate instead of WebTestClient
 * - @SpringBootTest with random port (integration test style from 1.5.x)
 * - No @WebMvcTest for slice testing
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductService productService;

    @Before
    public void setUp() {
        // OUTDATED: Manual test data setup instead of @Sql or test containers
    }

    @Test
    public void testGetAllProducts() {
        ResponseEntity<Map> response = restTemplate
            .withBasicAuth("admin", "admin123")
            .getForEntity("/inventory/api/products", Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetProductNotFound() {
        ResponseEntity<Map> response = restTemplate
            .withBasicAuth("admin", "admin123")
            .getForEntity("/inventory/api/products/99999", Map.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
