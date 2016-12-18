package com.gazuros.inventory.controller;

import com.gazuros.inventory.dao.InventoryDao;
import com.gazuros.inventory.model.Inventory;
import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${gazuros.kit.bonsai}")
    private String bonsaiKitsStr;

    private Multimap<String, Pair<Long, Integer>> kits = HashMultimap.create();

    @PostConstruct
    public void init() {

        System.out.println("============ START APP ===============");

        //bonsai kit
        fillKitsStr(bonsaiKitsStr);


        System.out.println("kitsMap: " + kits);

    }

    private void fillKitsStr(String kitsStr) {


        String[] productIdToQuantityPairs = kitsStr.split(",");
        for (String productIdToQuantityPairStr : productIdToQuantityPairs) {

            String[] productIdToQuantityPair = productIdToQuantityPairStr.split(":");
            kits.put(BONSAI_KIT, Pair.of(Long.valueOf(productIdToQuantityPair[0]), Integer.valueOf(productIdToQuantityPair[1])));  //pots => 5
        }
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
    public void removeNumBonsaiKitsFromInventory(@RequestParam String kitName, @RequestParam int numKitsToRemove) {

        System.out.println("removeNumBonsaiKitsFromInventory kitName: " + kitName + ", numKitsToRemove: " + numKitsToRemove);

        Collection<Pair<Long, Integer>> kitProducts = this.kits.get(kitName);
        for (Pair<Long, Integer> pair : kitProducts) {

            Long productId = pair.getFirst();
            Integer numItemsInKit = pair.getSecond();

            System.out.println("kitName: " + kitName + ", productId: " + productId + ", numItemsInKit: " + numItemsInKit);

            Inventory inventory = inventoryDao.findByProductId(productId);
            System.out.println("Found inventory: " + inventory + " for productId: " + productId);

            int newCount = inventory.getCount() - (numKitsToRemove*numItemsInKit);
            inventory.setCount(newCount);
            inventory.setLastUpdate(DateTime.now(DateTimeZone.UTC).getMillis());

            Inventory fromDb = inventoryDao.save(inventory);
            System.out.println("new inventory: " + fromDb);
        }
    }
}
