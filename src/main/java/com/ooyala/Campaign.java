package com.ooyala;

/**
 * Created by s_lor_000 on 9/30/2015.
 */
public class Campaign {

    private String customer;
    private Integer impressions;
    private int price;

    public Campaign(String customer, Integer impressions, int price) {
        this.customer = customer;
        this.impressions = impressions;
        this.price = price;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Integer getImpressions() {
        return impressions;
    }

    public void setImpressions(Integer impressions) {
        this.impressions = impressions;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "customer='" + customer + '\'' +
                ", impressions=" + impressions +
                ", price=" + price +
                '}';
    }
}
