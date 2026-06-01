package com.legacy.inventory.controller;

import com.legacy.inventory.model.Order;
import com.legacy.inventory.service.OrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = Logger.getLogger(OrderController.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        Map<String, Object> response = new HashMap<>();
        response.put("data", orders);
        response.put("count", orders.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            Order created = orderService.createOrder(order);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            Order updated = orderService.updateOrderStatus(id, status);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/status/{status}", method = RequestMethod.GET)
    public ResponseEntity<?> getByStatus(@PathVariable String status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public ResponseEntity<?> getReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            String report = orderService.generateOrderReport(start, end);
            Map<String, String> response = new HashMap<>();
            response.put("report", report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ParseException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid date format");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}
