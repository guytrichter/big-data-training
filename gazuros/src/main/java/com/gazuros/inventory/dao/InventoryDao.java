package com.gazuros.inventory.dao;

import com.gazuros.inventory.model.Inventory;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by guy on 12/17/16.
 */
@Transactional
public interface InventoryDao extends CrudRepository<Inventory, Long> {

    Inventory findByProductId(long productId);
}
