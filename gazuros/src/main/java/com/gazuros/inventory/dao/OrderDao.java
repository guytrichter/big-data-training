package com.gazuros.inventory.dao;

import com.gazuros.inventory.model.Order;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by guy on 12/17/16.
 */
@Transactional
public interface OrderDao extends CrudRepository<Order, Long> {

    List<Order> findByStatus(String status);

    List<Order> findByProductId(long productId);

    List<Order> findByProductIdAndStatus(long productId, String status);

    List<Order> findByDateTimestampGreaterThan(long dateTimestamp);

}
