package com.legacy.inventory.scheduler;

import com.legacy.inventory.model.Product;
import com.legacy.inventory.service.NotificationService;
import com.legacy.inventory.service.ProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * OUTDATED PATTERNS:
 * - Log4j 1.x
 * - Field injection
 * - SimpleDateFormat usage
 * - Hardcoded scheduling expressions
 * - No cloud-ready scheduling (should use distributed scheduler)
 * - Local file system operations
 */
public class InventoryScheduler {

    private static final Logger logger = Logger.getLogger(InventoryScheduler.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    @Autowired
    private ProductService productService;

    @Autowired
    private NotificationService notificationService;

    // OUTDATED: Hardcoded cron expression
    @Scheduled(cron = "0 0 6 * * ?")
    public void dailyLowStockCheck() {
        logger.info("Running daily low stock check at: " + new Date());
        try {
            List<Product> lowStockProducts = productService.getLowStockProducts(10);
            if (!lowStockProducts.isEmpty()) {
                notificationService.sendLowStockAlert(lowStockProducts);
                logger.info("Low stock alert sent for " + lowStockProducts.size() + " products");
            }
        } catch (Exception e) {
            logger.error("Error during low stock check: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // OUTDATED: Writing to local file system (not cloud-ready)
    @Scheduled(fixedRate = 86400000) // every 24 hours
    public void dailyReportGeneration() {
        logger.info("Generating daily report at: " + new Date());
        try {
            List<Product> products = productService.getAllProducts();
            // OUTDATED: Writing to local filesystem
            String filePath = "/var/reports/inventory_" + sdf.format(new Date()) + ".txt";
            notificationService.exportProductReport(products, filePath);
        } catch (Exception e) {
            logger.error("Error generating daily report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // OUTDATED: Manual cleanup task
    @Scheduled(fixedDelay = 3600000) // every hour
    public void cleanupExpiredProducts() {
        logger.info("Running expired product cleanup");
        try {
            List<Product> allProducts = productService.getAllProducts();
            // OUTDATED: Iterating instead of using streams/batch
            for (Product product : allProducts) {
                if (product.getExpiryDate() != null && product.getExpiryDate().before(new Date())) {
                    product.setIsActive(false);
                    product.setUpdatedDate(new Date());
                    productService.updateProduct(product.getId(), product);
                    logger.info("Deactivated expired product: " + product.getSku());
                }
            }
        } catch (Exception e) {
            logger.error("Error during cleanup: " + e.getMessage());
        }
    }
}
