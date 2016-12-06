package org.trichter.mongo.model;

/**
 * Created by guy on 12/5/16.
 */
public class Sale {

    private String saleStartDateStr;
    private String saleEndDateStr;
    private long saleStartDateTimestamp;
    private long saleEndDateTimestamp;
    private double salePrice;

    public String getSaleStartDateStr() {
        return saleStartDateStr;
    }

    public void setSaleStartDateStr(String saleStartDateStr) {
        this.saleStartDateStr = saleStartDateStr;
    }

    public String getSaleEndDateStr() {
        return saleEndDateStr;
    }

    public void setSaleEndDateStr(String saleEndDateStr) {
        this.saleEndDateStr = saleEndDateStr;
    }

    public long getSaleStartDateTimestamp() {
        return saleStartDateTimestamp;
    }

    public void setSaleStartDateTimestamp(long saleStartDateTimestamp) {
        this.saleStartDateTimestamp = saleStartDateTimestamp;
    }

    public long getSaleEndDateTimestamp() {
        return saleEndDateTimestamp;
    }

    public void setSaleEndDateTimestamp(long saleEndDateTimestamp) {
        this.saleEndDateTimestamp = saleEndDateTimestamp;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "saleStartDateStr='" + saleStartDateStr + '\'' +
                ", saleEndDateStr='" + saleEndDateStr + '\'' +
                ", saleStartDateTimestamp=" + saleStartDateTimestamp +
                ", saleEndDateTimestamp=" + saleEndDateTimestamp +
                ", salePrice=" + salePrice +
                '}';
    }
}
