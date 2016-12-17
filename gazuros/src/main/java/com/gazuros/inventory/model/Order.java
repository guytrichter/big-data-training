package com.gazuros.inventory.model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by guy on 12/17/16.
 */
@Entity
@Table(name = "order")
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "date_timestamp")
    private long dateTimestamp;

    @Column(name = "date_str")
    private String dateStr;

    @Column(name = "amount")
    private int amount;

    @Column(name = "status")
    private String status;

    @Column(name = "shipping_price")
    private double shippingPrice;

    @Column(name="destination")
    private String destination;

    public Order() {
        DateTime now = DateTime.now(DateTimeZone.UTC);
        this.dateTimestamp = now.getMillis();
        this.dateStr = now.toString();
    }

    public Order(Order other) {
        DateTime now = DateTime.now(DateTimeZone.UTC);
        this.dateTimestamp = now.getMillis();
        this.dateStr = now.toString();
        this.id = other.id;
        this.amount = other.amount;
        this.destination=other.destination;
        this.shippingPrice = other.shippingPrice;
        this.status=other.status;
        this.productId = other.productId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDateTimestamp() {
        return dateTimestamp;
    }

    public void setDateTimestamp(long dateTimestamp) {
        this.dateTimestamp = dateTimestamp;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", productId=" + productId +
                ", dateTimestamp=" + dateTimestamp +
                ", dateStr='" + dateStr + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", shippingPrice=" + shippingPrice +
                ", destination='" + destination + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return id == order.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }



}
