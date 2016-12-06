package org.trichter.mongo.model;

/**
 * Created by guy on 12/5/16.
 */
public class Stock {

    private int quantity;
    private String status;

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "quantity=" + quantity +
                ", status='" + status + '\'' +
                '}';
    }
}
