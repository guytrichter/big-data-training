package org.trichter.mongo.model;

/**
 * Created by guy on 12/5/16.
 */
public class Fees {

    private double shippingFeePercentage;

    public double getShippingFeePercentage() {
        return shippingFeePercentage;
    }

    public void setShippingFeePercentage(double shippingFeePercentage) {
        this.shippingFeePercentage = shippingFeePercentage;
    }

    @Override
    public String toString() {
        return "Fees{" +
                "shippingFeePercentage=" + shippingFeePercentage +
                '}';
    }
}
