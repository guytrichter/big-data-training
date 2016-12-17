package com.gazuros.inventory.controller;

import com.gazuros.inventory.dao.ProductDao;
import com.gazuros.inventory.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by guy on 12/17/16.
 */

@Controller
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @RequestMapping(value = "/products/create", method = RequestMethod.POST)
    @ResponseBody
    public Long createNewProduct(@RequestBody Product product) {

        System.out.println("Received product: " + product);
        Product fromDb = productDao.save(product);
        System.out.println("FromDB: " + fromDb);

        return fromDb.getId();
    }

    @RequestMapping(value = "/products/getProduct/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public Product getProduct(@PathVariable long productId) {

        System.out.println("getProduct for productId: " + productId);
        Product fromDb = productDao.findOne(productId);
        System.out.println("FromDB: " + fromDb);

        return fromDb;
    }

    @RequestMapping(value = "/products/getProducts", method = RequestMethod.GET)
    @ResponseBody
    public List<Product> getProducts() {

        System.out.println("getProducts");
        List<Product> fromDb = (List<Product>) productDao.findAll();
        System.out.println("FromDB: " + fromDb);

        return fromDb;
    }
}
