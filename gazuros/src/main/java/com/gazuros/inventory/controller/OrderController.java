package com.gazuros.inventory.controller;

import com.gazuros.inventory.dao.InventoryDao;
import com.gazuros.inventory.dao.OrderDao;
import com.gazuros.inventory.model.Inventory;
import com.gazuros.inventory.model.Order;
import com.google.common.base.Joiner;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by guy on 12/17/16.
 */
@Controller
public class OrderController {

    public static final String BACK_ORDER = "BACK_ORDER";
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private InventoryDao inventoryDao;

    @RequestMapping(value = "/orders/create", method = RequestMethod.POST)
    @ResponseBody
    public Long createNewOrder(@RequestBody Order order) {

        DateTime now = DateTime.now(DateTimeZone.UTC);
        order.setDateTimestamp(now.getMillis());
        order.setDateStr(now.toString());

        System.out.println("Received order: " + order);
        order.setStatus(BACK_ORDER);
        Order fromDb = orderDao.save(order);
        System.out.println("FromDB: " + fromDb);

        return fromDb.getId();
    }

    @RequestMapping(value = "/orders/getOrder/{orderId}", method = RequestMethod.GET)
    @ResponseBody
    public Order getOrder(@PathVariable long orderId) {

        System.out.println("getOrders for productId: " + orderId);
        Order fromDb = orderDao.findOne(orderId);
        System.out.println("FromDB: " + fromDb);

        return fromDb;
    }

    @Transactional
    @RequestMapping(value = "/orders/update/{orderId}", method = RequestMethod.PUT)
    @ResponseBody
    public Order updateOrder(@PathVariable long orderId, @RequestParam String status) {

        System.out.println("Received update for order: " + orderId + " with status: " + status);
        Order fromDb = orderDao.findOne(orderId);
        System.out.println("FromDB: " + fromDb);

        DateTime now = DateTime.now(DateTimeZone.UTC);
        fromDb.setDateTimestamp(now.getMillis());
        fromDb.setDateStr(now.toString());
        fromDb.setStatus(status);
        Order updatedOrderFromDb = orderDao.save(fromDb);
        System.out.println("UpdatedOrder: " + updatedOrderFromDb);

        System.out.println("Updating inventory for productId: " + updatedOrderFromDb.getProductId());
        Inventory inventory = inventoryDao.findByProductId(updatedOrderFromDb.getProductId());
        System.out.println("Inventory: " + inventory);

        if (null == inventory) {
            inventory = new Inventory();
            inventory.setProductId(updatedOrderFromDb.getProductId());
            inventory.setCount(0);
        }

        int newAmount = updatedOrderFromDb.getAmount() + inventory.getCount();
        inventory.setCount(newAmount);
        inventory.setLastUpdate(DateTime.now(DateTimeZone.UTC).getMillis());
        inventory.setPackager(updatedOrderFromDb.getPackager());
        Inventory newInventory = inventoryDao.save(inventory);
        System.out.println("newInventory: " + newInventory);

        return fromDb;
    }

    @RequestMapping(value = "/orders/getOrders", method = RequestMethod.GET)
    @ResponseBody
    public List<Order> getOrders() {

        System.out.println("getOrders");
        List<Order> fromDb = (List<Order>) orderDao.findAll();
        String listStr = Joiner.on(",").join(fromDb);
        System.out.println("FromDB: " + listStr);

        return fromDb;
    }

    @RequestMapping(value = "/orders/getOrdersByStatus", method = RequestMethod.GET)
    @ResponseBody
    public List<Order> getOrdersByStatus(@RequestParam String status) {

        System.out.println("getProductByStatus for status: " + status);
        List<Order> fromDb = orderDao.findByStatus(status);
        String listStr = Joiner.on(",").join(fromDb);
        System.out.println("FromDB: " + listStr);

        return fromDb;
    }

    @RequestMapping(value = "/orders/getOrdersByProductId", method = RequestMethod.GET)
    @ResponseBody
    public List<Order> getOrdersByProductId(@RequestParam long productId) {

        System.out.println("getProductByProductId for productId: " + productId);
        List<Order> fromDb = orderDao.findByProductId(productId);
        String listStr = Joiner.on(",").join(fromDb);
        System.out.println("FromDB: " + listStr);

        return fromDb;
    }


    @RequestMapping(value = "/orders/getOrdersByStatusAndProductId", method = RequestMethod.GET)
    @ResponseBody
    public List<Order> getOrdersByStatusAndProductId(@RequestParam String status, @RequestParam long productId) {

        System.out.println("getOrdersByStatusAndProductId for status: " + status + " and productId" + productId);
        List<Order> fromDb = orderDao.findByProductIdAndStatus(productId, status);
        String listStr = Joiner.on(",").join(fromDb);
        System.out.println("FromDB: " + listStr);

        return fromDb;
    }

    @RequestMapping(value = "/orders/getOrdersByTime", method = RequestMethod.GET)
    @ResponseBody
    public List<Order> getOrdersNumDaysBack(@RequestParam int numDaysBack) {

        DateTime dateTime = DateTime.now(DateTimeZone.UTC).minusDays(numDaysBack);
        long millis = dateTime.getMillis();

        System.out.println("getOrdersNumDaysBack numDaysBack: " + numDaysBack);
        List<Order> fromDb = orderDao.findByDateTimestampGreaterThan(millis);
        String listStr = Joiner.on(",").join(fromDb);
        System.out.println("FromDB: " + listStr);

        return fromDb;
    }

}
