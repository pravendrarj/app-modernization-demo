package com.legacy.inventory.service;

import com.legacy.inventory.model.Product;
import com.legacy.inventory.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * OUTDATED PATTERNS:
 * - JUnit 4 (should be JUnit 5 / Jupiter)
 * - @RunWith(MockitoJUnitRunner.class) instead of @ExtendWith(MockitoExtension.class)
 * - Uses org.junit.Assert instead of org.junit.jupiter.api.Assertions
 * - Uses @Before instead of @BeforeEach
 * - No AssertJ or Hamcrest matchers
 * - MockitoJUnitRunner from deprecated package
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @Before
    public void setUp() {
        testProduct = new Product("Test Product", "SKU-TEST-001", 29.99, 100);
        testProduct.setId(1L);
        testProduct.setCategory("Electronics");
        testProduct.setCreatedDate(new Date());
        testProduct.setIsActive(true);
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getProductName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductById() {
        when(productRepository.findOne(1L)).thenReturn(testProduct);

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getProductName());
    }

    @Test
    public void testGetProductByIdNotFound() {
        when(productRepository.findOne(999L)).thenReturn(null);

        Product result = productService.getProductById(999L);

        assertNull(result);
    }

    @Test
    public void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product newProduct = new Product("New Product", null, 19.99, 50);
        Product result = productService.createProduct(newProduct);

        assertNotNull(result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateProductNotFound() {
        when(productRepository.findOne(999L)).thenReturn(null);

        productService.updateProduct(999L, testProduct);
    }

    @Test
    public void testDeleteProduct() {
        when(productRepository.findOne(1L)).thenReturn(testProduct);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(testProduct);
    }

    @Test
    public void testGetInventorySummary() {
        Product p1 = new Product("P1", "SKU1", 10.0, 50);
        p1.setCategory("Electronics");
        Product p2 = new Product("P2", "SKU2", 20.0, 30);
        p2.setCategory("Electronics");
        Product p3 = new Product("P3", "SKU3", 15.0, 20);
        p3.setCategory("Clothing");

        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));

        Map<String, Integer> summary = productService.getInventorySummary();

        assertEquals(Integer.valueOf(80), summary.get("Electronics"));
        assertEquals(Integer.valueOf(20), summary.get("Clothing"));
    }
}
