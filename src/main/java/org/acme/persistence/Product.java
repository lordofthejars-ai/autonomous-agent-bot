package org.acme.persistence;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

public class Product extends PanacheMongoEntity {

    public String name;
    public double price;
    public int weight;
    public String monitor;
    public String resolution;
    public String description;

    public static Product findByName(String productName) {
        return find("name", productName).singleResult();
    }

}
