package com.gazuros.inventory.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Column(name = "num_items_per_box")
    private int numItemsPerBox;

    @Column(name = "num_boxes")
    private int numBoxes;
    
    @Column(name = "status")
    private String status;

    @Column(name = "shipping_price")
    private double shippingPrice;

    @Column(name="packager")
    private String packager;

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

    public String getPackager() {
        return packager;
    }

    public void setPackager(String packager) {
        this.packager = packager;
    }
    
    public int getNumBoxes() {
		return numBoxes;
	}
    
    public void setNumBoxes(int numBoxes) {
		this.numBoxes = numBoxes;
	}
    
    public int getNumItemsPerBox() {
		return numItemsPerBox;
	}
    
    public void setNumItemsPerBox(int numItemsPerBox) {
		this.numItemsPerBox = numItemsPerBox;
	}

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", productId=" + productId +
                ", dateTimestamp=" + dateTimestamp +
                ", dateStr='" + dateStr + '\'' +
                ", numBoxes='" + numBoxes + '\'' +
                ", numItemsPerBox='" + numItemsPerBox + '\'' +
                ", status='" + status + '\'' +
                ", shippingPrice=" + shippingPrice +
                ", packager='" + packager + '\'' +
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
