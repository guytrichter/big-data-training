package org.trichter.mongo;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.trichter.mongo.model.Fees;
import org.trichter.mongo.model.ListingConfiguration;
import org.trichter.mongo.model.ListingData;
import org.trichter.mongo.model.Sale;
import org.trichter.mongo.repository.ListingsDataRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guy on 12/5/16.
 */

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private ListingsDataRepository repository;

    @Autowired
    private MongoClient mongoClient;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        repository.deleteAll();

        // save a couple of customers
        ListingData listingData = new ListingData(54321L);
        listingData.setAccountId(349);

        ListingConfiguration listingConf = new ListingConfiguration();
        listingConf.setFloorPrice(15D);
        listingConf.setCeilingPrice(30D);
        listingConf.setMapPrice(2D);

        Sale sale = new Sale();
        sale.setSaleEndDateStr("21.12.2016");
        sale.setSaleEndDateTimestamp(1482278400000L);
        sale.setSaleStartDateStr("30.11.2016");
        sale.setSaleStartDateTimestamp(1480464000000L);
        sale.setSalePrice(17D);

        listingConf.setSale(sale);

        Map<Integer, Double> businessPriceMap = new HashMap<>();
        businessPriceMap.put(1, 30.0D);
        businessPriceMap.put(2, 60.0D);
        businessPriceMap.put(3, 90.0D);

        listingConf.setBusinessPriceMap(businessPriceMap);

        listingData.setListingConfiguration(listingConf);

        Fees fees = new Fees();
        fees.setShippingFeePercentage(3.2D);
        listingData.setFees(fees);


        repository.save(listingData);

        ListingData fromDb = repository.findByListingId(54321L);
        System.out.println(fromDb);

//        // fetch all customers
//        System.out.println("Customers found with findAll():");
//        System.out.println("-------------------------------");
//        for (Customer customer : repository.findAll()) {
//            System.out.println(customer);
//        }
//        System.out.println();
//
//        // fetch an individual customer
//        System.out.println("Customer found with findByFirstName('Alice'):");
//        System.out.println("--------------------------------");
//        System.out.println(repository.findByFirstName("Alice"));
//
//        System.out.println("Customers found with findByLastName('Smith'):");
//        System.out.println("--------------------------------");
//        for (Customer customer : repository.findByLastName("Smith")) {
//            System.out.println(customer);
//        }

    }

}
