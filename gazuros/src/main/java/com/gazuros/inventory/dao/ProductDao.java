package com.gazuros.inventory.dao;

import com.gazuros.inventory.model.Product;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
/**
 * Created by guy on 12/17/16.
 */
@Transactional
public interface ProductDao extends CrudRepository<Product, Long> {
}
