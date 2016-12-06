package org.trichter.mongo.model;

import java.util.Map;

/**
 * Created by guy on 12/5/16.
 */
public class ListingConfiguration {

    private double floorPrice;
    private double ceilingPrice;
    private double mapPrice;
    private Sale sale;
    private Map<Integer, Double> businessPriceMap;

    public double getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(double floorPrice) {
        this.floorPrice = floorPrice;
    }

    public double getCeilingPrice() {
        return ceilingPrice;
    }

    public void setCeilingPrice(double ceilingPrice) {
        this.ceilingPrice = ceilingPrice;
    }

    public double getMapPrice() {
        return mapPrice;
    }

    public void setMapPrice(double mapPrice) {
        this.mapPrice = mapPrice;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public void setBusinessPriceMap(Map<Integer, Double> businessPriceMap) {
        this.businessPriceMap = businessPriceMap;
    }

    public Map<Integer, Double> getBusinessPriceMap() {
        return businessPriceMap;
    }

    @Override
    public String toString() {
        return "ListingConfiguration{" +
                "floorPrice=" + floorPrice +
                ", ceilingPrice=" + ceilingPrice +
                ", mapPrice=" + mapPrice +
                ", sale=" + sale +
                ", businessPriceMap=" + businessPriceMap +
                '}';
    }
}
