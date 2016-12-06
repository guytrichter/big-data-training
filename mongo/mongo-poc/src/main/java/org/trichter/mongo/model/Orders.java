package org.trichter.mongo.model;

/**
 * Created by guy on 12/5/16.
 */
public class Orders {

    private int numOrders;

    public int getNumOrders() {
        return numOrders;
    }

    public void setNumOrders(int numOrders) {
        this.numOrders = numOrders;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "numOrders=" + numOrders +
                '}';
    }
}
