package com.legacy.inventory.service;

import com.legacy.inventory.model.Order;
import com.legacy.inventory.model.OrderItem;
import com.legacy.inventory.model.Product;
import com.legacy.inventory.repository.OrderRepository;
import com.legacy.inventory.repository.ProductRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * OUTDATED PATTERNS:
 * - Log4j 1.x
 * - Field injection
 * - java.util.Date
 * - Manual order number generation
 * - No validation framework usage
 * - Raw RuntimeExceptions
 */
@Service
@Transactional
public class OrderService {

    private static final Logger logger = Logger.getLogger(OrderService.class);
    private static final SimpleDateFormat orderDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findOne(id);
    }

    public Order getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    public Order createOrder(Order order) {
        logger.info("Creating new order for customer: " + order.getCustomerName());

        // OUTDATED: Manual order number generation
        String orderNumber = "ORD-" + orderDateFormat.format(new Date()) + "-" + new Random().nextInt(9999);
        order.setOrderNumber(orderNumber);
        order.setOrderDate(new Date());
        order.setStatus("PENDING");

        // OUTDATED: Manual total calculation without streams
        double total = 0;
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                Product product = productRepository.findOne(item.getProduct().getId());
                if (product == null) {
                    throw new RuntimeException("Product not found: " + item.getProduct().getId());
                }

                // OUTDATED: No stock validation
                if (product.getQuantity() < item.getQuantity()) {
                    throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
                }

                item.setUnitPrice(product.getPrice());
                item.setSubtotal(product.getPrice() * item.getQuantity());
                item.setOrder(order);
                total += item.getSubtotal();

                // Update stock
                product.setQuantity(product.getQuantity() - item.getQuantity());
                product.setUpdatedDate(new Date());
                productRepository.save(product);
            }
        }

        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);
        logger.info("Order created: " + savedOrder.getOrderNumber());
        return savedOrder;
    }

    // OUTDATED: String-based status management
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findOne(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found: " + orderId);
        }

        // OUTDATED: String comparison for status validation
        List<String> validStatuses = Arrays.asList("PENDING", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED");
        if (!validStatuses.contains(status)) {
            throw new RuntimeException("Invalid status: " + status);
        }

        order.setStatus(status);
        if ("SHIPPED".equals(status)) {
            order.setShippedDate(new Date());
        }

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getOrdersByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }

    // OUTDATED: Manual report generation with string building
    public String generateOrderReport(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findByDateRange(startDate, endDate);

        StringBuilder report = new StringBuilder();
        report.append("Order Report\n");
        report.append("============\n");
        report.append("Period: ").append(orderDateFormat.format(startDate))
              .append(" to ").append(orderDateFormat.format(endDate)).append("\n\n");

        double totalRevenue = 0;
        int totalOrders = 0;

        // OUTDATED: For-each loop with manual accumulation instead of streams
        for (Order order : orders) {
            report.append("Order: ").append(order.getOrderNumber())
                  .append(" | Status: ").append(order.getStatus())
                  .append(" | Total: $").append(order.getTotalAmount()).append("\n");
            totalRevenue += order.getTotalAmount();
            totalOrders++;
        }

        report.append("\nTotal Orders: ").append(totalOrders);
        report.append("\nTotal Revenue: $").append(totalRevenue);

        return report.toString();
    }
}
