package com.gazuros.inventory.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by guy on 12/17/16.
 */
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @Column(name = "inventory_id")
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "count")
    private int count;

    @Column(name = "last_update")
    private long lastUpdate;

    @Column(name = "packager")
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    public String getPackager() {
        return packager;
    }

    public void setPackager(String packager) {
        this.packager = packager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Inventory inventory = (Inventory) o;

        if (id != inventory.id) return false;
        if (productId != inventory.productId) return false;
        if (count != inventory.count) return false;
        return lastUpdate == inventory.lastUpdate;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        result = 31 * result + count;
        result = 31 * result + (int) (lastUpdate ^ (lastUpdate >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", productId=" + productId +
                ", count=" + count +
                ", lastUpdate=" + lastUpdate +
                ", packager=" + packager +
                '}';
    }
}
