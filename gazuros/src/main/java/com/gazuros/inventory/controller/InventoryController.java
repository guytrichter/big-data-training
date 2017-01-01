package com.gazuros.inventory.controller;

import com.gazuros.inventory.dao.InventoryDao;
import com.gazuros.inventory.dao.ProductDao;
import com.gazuros.inventory.model.Inventory;
import com.gazuros.inventory.model.Product;
import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by guy on 12/17/16.
 */
@Controller
public class InventoryController {

    private static final String BONSAI_KIT = "BONSAI";
    private static final String HERBS_KIT = "HERBS";
    private static final String CRAZY_GARDEN = "CRAZY_GARDEN";

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private ProductDao productDao;

    @Value("${gazuros.kit.bonsai}")
    private String bonsaiKitsStr;
    
    @Value("${gazuros.kit.herbs}")
    private String herbsKitStr;

    @Value("${gazuros.kit.crazy.garden}")
    private String crazyGardernKitStr;
    
    private Multimap<String, Pair<Long, Integer>> kits = HashMultimap.create();

    @PostConstruct
    public void init() {

        System.out.println("============ START APP ===============");

        //bonsai kit
        fillKitsStr(BONSAI_KIT, bonsaiKitsStr);
        fillKitsStr(HERBS_KIT, herbsKitStr);
        fillKitsStr(CRAZY_GARDEN, crazyGardernKitStr);

        System.out.println("kitsMap: " + kits);

    }

    private void fillKitsStr(String kitName, String kitsStr) {

        String[] productIdToQuantityPairs = kitsStr.split(",");
        for (String productIdToQuantityPairStr : productIdToQuantityPairs) {

            String[] productIdToQuantityPair = productIdToQuantityPairStr.split(":");
            kits.put(kitsStr, Pair.of(Long.valueOf(productIdToQuantityPair[0]), Integer.valueOf(productIdToQuantityPair[1])));  //pots => 5
        }
    }

    @RequestMapping(value = "/inventory/getKitNames", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getKitNames() {
        return Lists.newArrayList(BONSAI_KIT, HERBS_KIT, CRAZY_GARDEN);
    }

    @RequestMapping(value = "/inventory/getCurrentInventory", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Integer> getCurrentInventory() {

        Map<String, Integer> result = new HashMap<String, Integer>();

        System.out.println("getCurrentInventory");
        List<Inventory>  fromDb = (List<Inventory>) inventoryDao.findAll();
        System.out.println("FromDB: " + Joiner.on(",").join(fromDb));

        for (Inventory inventory : fromDb) {
            Product product = productDao.findOne(inventory.getProductId());
            if (null != product)
                result.put(product.getName(), inventory.getCount());
            else
                System.out.println("Can't find product with Inventory: " + inventory);
        }

        return result;
    }

    @RequestMapping(value = "/inventory/updateCurrentInventory/{productId}", method = RequestMethod.PUT)
    @ResponseBody
    public boolean updateCurrentInventory(@PathVariable long productId, @RequestParam int count) {

        System.out.println("updateCurrentInventory");
        Inventory fromDb =  inventoryDao.findByProductId(productId);
        System.out.println("FromDB: " + fromDb);

        fromDb.setCount(count);
        Inventory newObj = inventoryDao.save(fromDb);

        System.out.println("new obj: " + newObj);

        return (null != newObj);
    }
    
    @RequestMapping(value = "/inventory/removeNumBoxes", method = RequestMethod.PUT)
    @ResponseBody
    public int removeNumBoxes(@RequestParam int numBoxesToRemove) {

        System.out.println("removeNumBoxes: " + numBoxesToRemove);
        List<Inventory>  fromDb = (List<Inventory>) inventoryDao.findAll();
        System.out.println("FromDB: " + Joiner.on(",").join(fromDb));
        
        Inventory inventory = inventoryDao.findByProductId(72); //master_carton prodictId
        System.out.println("Found inventory: " + inventory);

        int newCount = inventory.getCount() - numBoxesToRemove;
        inventory.setCount(newCount);
        inventory.setLastUpdate(DateTime.now(DateTimeZone.UTC).getMillis());

        Inventory newBox = inventoryDao.save(inventory);
        System.out.println("new inventory: " + fromDb);

        return newBox.getCount();
    }

    @Transactional
    @RequestMapping(value = "/inventory/removeNumKitsFromInventory", method = RequestMethod.PUT)
    @ResponseBody
    public int removeNumKitsFromInventory(@RequestParam String kitName, @RequestParam int numKitsToRemove) {

        System.out.println("removeNumBonsaiKitsFromInventory kitName: " + kitName + ", numKitsToRemove: " + numKitsToRemove);

        Collection<Pair<Long, Integer>> kitProducts = this.kits.get(kitName);
        for (Pair<Long, Integer> pair : kitProducts) {

            Long productId = pair.getFirst();
            Integer numItemsInKit = pair.getSecond();

            System.out.println("kitName: " + kitName + ", productId: " + productId + ", numItemsInKit: " + numItemsInKit);

            Inventory inventory = inventoryDao.findByProductId(productId);
            if (null == inventory) {
                throw new RuntimeException("Could not find product: " + productId);
            }

            System.out.println("Found inventory: " + inventory + " for productId: " + productId);

            int newCount = inventory.getCount() - (numKitsToRemove*numItemsInKit);
            inventory.setCount(newCount);
            inventory.setLastUpdate(DateTime.now(DateTimeZone.UTC).getMillis());

            Inventory fromDb = inventoryDao.save(inventory);
            System.out.println("new inventory: " + fromDb);
        }

        return -1;
    }
}
