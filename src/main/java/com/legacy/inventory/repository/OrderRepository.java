package com.legacy.inventory.repository;

import com.legacy.inventory.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByOrderNumber(String orderNumber);

    @Query(value = "SELECT * FROM orders WHERE status = :status", nativeQuery = true)
    List<Order> findByStatus(@Param("status") String status);

    @Query(value = "SELECT * FROM orders WHERE customer_email = :email", nativeQuery = true)
    List<Order> findByCustomerEmail(@Param("email") String email);

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :start AND :end")
    List<Order> findByDateRange(@Param("start") Date start, @Param("end") Date end);
}
