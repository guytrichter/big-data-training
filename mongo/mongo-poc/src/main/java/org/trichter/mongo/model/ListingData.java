package org.trichter.mongo.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by guy on 12/5/16.
 */
@Document(collection = "listings_data")
public class ListingData {

    @Id
    private long listingId;
    private long timestamp;
    private int accountId;
    private String marketplace;
    private ListingConfiguration listingConfiguration;
    private Orders orders;
    private Stock stock;
    private Fees fees;

    public ListingData(long listingId) {
        this.listingId = listingId;
        this.timestamp = DateTime.now(DateTimeZone.UTC).getMillis();
    }

    public long getListingId() {
        return listingId;
    }

    public void setListingId(long listingId) {
        this.listingId = listingId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getMarketplace() {
        return marketplace;
    }

    public void setMarketplace(String marketplace) {
        this.marketplace = marketplace;
    }

    public ListingConfiguration getListingConfiguration() {
        return listingConfiguration;
    }

    public void setListingConfiguration(ListingConfiguration listingConfiguration) {
        this.listingConfiguration = listingConfiguration;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Fees getFees() {
        return fees;
    }

    public void setFees(Fees fees) {
        this.fees = fees;
    }

    @Override
    public String toString() {
        return "ListingData{" +
                "listingId=" + listingId +
                ", timestamp=" + timestamp +
                ", accountId=" + accountId +
                ", marketplace='" + marketplace + '\'' +
                ", listingConfiguration=" + listingConfiguration +
                ", orders=" + orders +
                ", stock=" + stock +
                ", fees=" + fees +
                '}';
    }
}
