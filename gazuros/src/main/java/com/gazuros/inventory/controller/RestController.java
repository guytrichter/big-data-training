package com.gazuros.inventory.controller;

import com.gazuros.inventory.model.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by guy on 12/17/16.
 */
@Controller
public class RestController {

    @RequestMapping("/health")
    @ResponseBody
    public boolean health() {
        return true;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }
//    public static void main(String[] args) {
//
//        Order order = new Order();
//        order.setProductId(2L);
//        order.setAmount(500);
//        order.setDestination("NY");
//        order.setShippingPrice(13D);
//        order.setStatus("BACK_ORDER");
//
//        System.out.println(order);
//    }


}
