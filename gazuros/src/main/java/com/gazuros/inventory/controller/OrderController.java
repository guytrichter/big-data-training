package com.gazuros.inventory.controller;

import com.gazuros.inventory.dao.InventoryDao;
import com.gazuros.inventory.dao.OrderDao;
import com.gazuros.inventory.dao.ProductDao;
import com.gazuros.inventory.model.Inventory;
import com.gazuros.inventory.model.Order;
import com.gazuros.inventory.model.Product;
import com.gazuros.inventory.utils.EmailUtils;
import com.google.common.base.Joiner;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guy on 12/17/16.
 */
@Controller
public class OrderController {

    public static final String BACK_ORDER = "BACK_ORDER";
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private InventoryDao inventoryDao;

    @RequestMapping(value = "/orders/create", method = RequestMethod.POST)
    @ResponseBody
    public Long createNewOrder(@RequestBody Order order) {

        //set time here or get from client???
        if (order.getProductId() == 0L || order.getDateTimestamp() == 0) {
            throw new RuntimeException("Invalid order");
        }

        order.setDateStr(order.getDateStr().substring(0, order.getDateStr().indexOf('T')));

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

        int newAmount = (updatedOrderFromDb.getNumBoxes() * updatedOrderFromDb.getNumItemsPerBox()) + inventory.getCount();
        inventory.setCount(newAmount);
        inventory.setLastUpdate(DateTime.now(DateTimeZone.UTC).getMillis());
        inventory.setPackager(updatedOrderFromDb.getPackager());
        Inventory newInventory = inventoryDao.save(inventory);
        System.out.println("newInventory: " + newInventory);
        
        Product product = productDao.findOne(updatedOrderFromDb.getProductId());
        
        //send mail
        String mailSmtpHost = "smtp.gmail.com";
        String mailTo = "danny@gazuros.com";
        String cc = "eyal@gazuros.com";
        String mailFrom = "kits.gazuros@gmail.com";
        String mailSubject = "Confirmed " + newAmount + " of product: " + product.getName(); 
        String mailText = "successfully received inventory";

        EmailUtils.sendEmail(mailTo, cc, mailFrom, mailSubject, mailText, mailSmtpHost);
        
        System.out.println("sent email");

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
    public List<ViewBackOrder> getOrdersByStatus(@RequestParam String status) {

        System.out.println("getProductByStatus for status: " + status);
        List<Order> fromDb = orderDao.findByStatus("BACK_ORDER");
        System.out.println("Collected " + fromDb.size() + " orders back order from db");
        List<ViewBackOrder> res = new ArrayList<ViewBackOrder>();

        for (Order order :fromDb) {

            Product product = productDao.findOne(order.getProductId());
            if (null == product) {
                System.out.println("order without product???");
                continue;
            }

            ViewBackOrder viewBackOrder = new ViewBackOrder();
            viewBackOrder.dateStr = order.getDateStr();
            viewBackOrder.dateTimestamp = order.getDateTimestamp();
            viewBackOrder.numBoxes = order.getNumBoxes();
            viewBackOrder.numItemsPerBox = order.getNumItemsPerBox();
            viewBackOrder.status = order.getStatus();
            viewBackOrder.shippingPrice = order.getShippingPrice();
            viewBackOrder.name = product.getName();
            viewBackOrder.id = order.getId();

            res.add(viewBackOrder);
        }


        return res;
    }

    /**
     * Product Name	Expected Date of Arrival
     * Number of Boxes
     * Number of Units Per Box
     * Total Number of Units To Recieve
     * Confirm You Recieved Order
     * @return
     */
    public class ViewBackOrder {
        public long id;
        public String dateStr;
        public long dateTimestamp;
        public int numBoxes;
        public int numItemsPerBox;
        public String packager;
        public String name;
        public double shippingPrice;
        public String status;
    }

    @RequestMapping(value = "/orders/getBackOrdersWithProductName", method = RequestMethod.GET)
    @ResponseBody
    public List<OrderResult> getBackOrdersWithProductName() {

        System.out.println("getBackOrdersWithProductName");
        List<Order> fromDb = orderDao.findByStatus(BACK_ORDER);
        String listStr = Joiner.on(",").join(fromDb);
        System.out.println("FromDB: " + listStr);

        List<OrderResult> result = new ArrayList<OrderResult>();
        for (Order order : fromDb) {
            Product product = productDao.findOne(order.getProductId());
            if (null == product) {
                System.out.println("order without product???");
                continue;
            }

            String productName = product.getName();
            OrderResult orderResult = new OrderResult();
            orderResult.orderId = order.getId();
            orderResult.dateTimestamp = order.getDateTimestamp();
            orderResult.dateStr = order.getDateStr();
            orderResult.numBoxes = order.getNumBoxes();
            orderResult.numItemsPerBox = order.getNumItemsPerBox();
            orderResult.productName = productName;
            orderResult.totalNumberOfItems = order.getNumItemsPerBox() * order.getNumBoxes();
            result.add(orderResult);
        }

        return result;
    }

    public static class OrderResult {
        public long orderId;
        public long dateTimestamp;
        public String dateStr;
        public int numItemsPerBox;
        public int numBoxes;
        public int totalNumberOfItems;
        public String productName;
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
