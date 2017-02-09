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
    public static final int BOX_PRODUCT_ID = 72;
    public static final String NOT_UNIQUE_TO_ANY_KIT = "Not unique to any kit";

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

        //insert products with no inventory as 0
        Iterable<Product> allProducts = productDao.findAll();
        for (Product product : allProducts) {

            Inventory inventory = inventoryDao.findByProductId(product.getId());
            if (null == inventory) {
                inventory = new Inventory();
                inventory.setProductId(product.getId());
                inventory.setCount(0);
                inventory.setLastUpdate(DateTime.now(DateTimeZone.UTC).getMillis());
                inventory.setPackager("ny");  //default for now
                Inventory fromDb = inventoryDao.save(inventory);
                System.out.println("Created new inventory for: " + product.getName() + " - " + fromDb);
            }
        }
    }

    private void fillKitsStr(String kitName, String kitsStr) {

        String[] productIdToQuantityPairs = kitsStr.split(",");
        for (String productIdToQuantityPairStr : productIdToQuantityPairs) {

            String[] productIdToQuantityPair = productIdToQuantityPairStr.split(":");
            kits.put(kitName, Pair.of(Long.valueOf(productIdToQuantityPair[0]), Integer.valueOf(productIdToQuantityPair[1])));  //pots => 5
        }
    }

    @RequestMapping(value = "/inventory/getKitNames", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getKitNames() {
        return Lists.newArrayList(kits.asMap().keySet());
    }

    @RequestMapping(value = "/inventory/getCurrentInventory", method = RequestMethod.GET)
    @ResponseBody
    public List<InventoryResult> getCurrentInventory() {

        Set<InventoryResult> result = new HashSet<InventoryResult>();

        System.out.println("getCurrentInventory");
        List<Inventory>  fromDb = (List<Inventory>) inventoryDao.findAll();
        System.out.println("FromDB: " + Joiner.on(",").join(fromDb));

        for (Inventory inventory : fromDb) {
            Product product = productDao.findOne(inventory.getProductId());
            if (null != product) {
                InventoryResult inventoryResult = new InventoryResult();
                inventoryResult.productId = product.getId();
                inventoryResult.productName = product.getName();
                inventoryResult.count = inventory.getCount();
                inventoryResult.requiredCountRed = product.getRequiredCountRed();

                Map<String, Collection<Pair<Long, Integer>>> kitsMap = kits.asMap();
                boolean appearedInPrev = false;
                boolean appearsInAnyKit = false;
                for (String kitName : kitsMap.keySet()) {
                    Collection<Pair<Long, Integer>> values = kitsMap.get(kitName);
                    for (Pair<Long, Integer> val : values) {
                        long productId = val.getFirst();
                        if (productId == inventoryResult.productId) {

                            if (appearedInPrev) {
                                inventoryResult.kitName= NOT_UNIQUE_TO_ANY_KIT;
                            } else {

                                //first kit this product is found in
                                appearedInPrev = true;
                                inventoryResult.kitName = kitName;
                            }

                            appearsInAnyKit = true;
                            result.add(inventoryResult);
                        } else {
//                            System.out.println("Skip product");
                        }
                    }
                }

                if (!appearsInAnyKit) {

                    //like master carton
                    inventoryResult.kitName= NOT_UNIQUE_TO_ANY_KIT;
                    result.add(inventoryResult);
                }

            }
        }

        return new ArrayList<InventoryResult>(result);
    }

    public static class InventoryResult {
        public String kitName;
        public long productId;
        public int requiredCountRed;
        public String productName;
        public int count;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InventoryResult that = (InventoryResult) o;

            return productId == that.productId;

        }

        @Override
        public int hashCode() {
            return (int) (productId ^ (productId >>> 32));
        }
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

        if (null == newObj) {
            throw new RuntimeException("newObj is null");
        }

        return true;
    }
    
    @RequestMapping(value = "/inventory/removeNumBoxes", method = RequestMethod.PUT)
    @ResponseBody
    public int removeNumBoxes(@RequestParam int numBoxesToRemove) {

        System.out.println("removeNumBoxes: " + numBoxesToRemove);
        List<Inventory>  fromDb = (List<Inventory>) inventoryDao.findAll();
        System.out.println("FromDB: " + Joiner.on(",").join(fromDb));
        
        Inventory inventory = inventoryDao.findByProductId(BOX_PRODUCT_ID); //master_carton prodictId
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
        if (kitProducts.isEmpty()) {
            throw new RuntimeException("Can't find kit: " + kitName);
        }

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
            if (newCount < 0) {
                throw new RuntimeException("Not enough stock of product: " + productId);
            }

            inventory.setCount(newCount);
            inventory.setLastUpdate(DateTime.now(DateTimeZone.UTC).getMillis());

            Inventory fromDb = inventoryDao.save(inventory);
            System.out.println("new inventory: " + fromDb);
        }

        return -1;
    }
}
