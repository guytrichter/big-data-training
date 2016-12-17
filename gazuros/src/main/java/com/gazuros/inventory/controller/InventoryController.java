package com.gazuros.inventory.controller;

import com.gazuros.inventory.dao.InventoryDao;
import com.gazuros.inventory.dao.ProductDao;
import com.gazuros.inventory.model.Inventory;
import com.gazuros.inventory.model.Product;
import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

/**
 * Created by guy on 12/17/16.
 */
@Controller
public class InventoryController {

    private static final String BONSAI_KIT = "BONSAI";

    @Autowired
    private InventoryDao inventoryDao;

    private Multimap<String, Pair<Long, Integer>> kits = HashMultimap.create();

    @PostConstruct
    public void init() {

        //bonsai kit
        kits.put(BONSAI_KIT, Pair.of(2L, 5));  //pots => 5
        kits.put(BONSAI_KIT, Pair.of(3L, 1));  //box => 1


        System.out.println("============ START APP ===============");

    }

    @RequestMapping(value = "/inventory/getCurrentInventory", method = RequestMethod.GET)
    @ResponseBody
    public List<Inventory> getCurrentInventory() {

        System.out.println("getCurrentInventory");
        List<Inventory>  fromDb = (List<Inventory>) inventoryDao.findAll();
        System.out.println("FromDB: " + Joiner.on(",").join(fromDb));

        return fromDb;
    }

    @Transactional
    @RequestMapping(value = "/inventory/removeNumBonsaiKitsFromInventory", method = RequestMethod.PUT)
    @ResponseBody
    public void removeNumBonsaiKitsFromInventory(@RequestParam int numBonsaiKitsToRemove) {

        Collection<Pair<Long, Integer>> bonsaiKitProducts = this.kits.get(BONSAI_KIT);
        for (Pair<Long, Integer> pair : bonsaiKitProducts) {

            Long productId = pair.getFirst();
            Integer numItemsInKit = pair.getSecond();

            System.out.println("productId: " + productId + ", numItemsInKit: " + numItemsInKit);

            Inventory inventory = inventoryDao.findByProductId(productId);
            System.out.println("Found inventory: " + inventory + " for productId: " + productId);

            int newCount = inventory.getCount() - (numBonsaiKitsToRemove*numItemsInKit);
            inventory.setCount(newCount);
            inventory.setLastUpdate(DateTime.now(DateTimeZone.UTC).getMillis());

            Inventory fromDb = inventoryDao.save(inventory);
            System.out.println("new inventory: " + fromDb);
        }
    }
}
