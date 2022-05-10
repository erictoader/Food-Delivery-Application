package com.erictoader.fooddelivery.model;

import java.util.LinkedList;
import java.util.List;

public class CompositeProduct extends MenuItem{
    private String title;
    private Double rating;
    private Integer calories;
    private Integer protein;
    private Integer fat;
    private Integer sodium;
    private Integer price;
    private List<MenuItem> products;

    public CompositeProduct() {
    }

    public CompositeProduct(String title, Double rating, Integer calories, Integer protein, Integer fat, Integer sodium, Integer price) {
        this.title = title;
        this.rating = rating;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.sodium = sodium;
        this.price = price;
        this.products = new LinkedList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public Integer getSodium() {
        return sodium;
    }

    public void setSodium(Integer sodium) {
        this.sodium = sodium;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<MenuItem> getProducts() {
        return products;
    }

    public void setProducts(List<MenuItem> products) {
        this.products = products;
        calculateMacronutrients(products);
    }

    private void calculateMacronutrients(List<MenuItem> products) {
        int cal = 0, pro = 0, fa = 0, sod = 0;
        for(MenuItem mi : products) {
            cal += mi.getCalories();
            pro += mi.getProtein();
            fa += mi.getFat();
            sod += mi.getSodium();
        }
        setCalories(cal);
        setProtein(pro);
        setFat(fa);
        setSodium(sod);
    }

    @Override
    public Integer computePrice() {
        this.price = 0;
        for(MenuItem mi : products) {
            this.price += mi.computePrice();
        }
        return this.price;
    }

    @Override
    public int compareTo(MenuItem mi) {
        String thisFormatted = this.title.replaceAll("\\s", "");
        String miFormatted = mi.getTitle().replaceAll("\\s", "");
        return thisFormatted.toLowerCase().compareTo(miFormatted.toLowerCase());
    }

    public String getContains() {
        StringBuilder sb = new StringBuilder();
        for(MenuItem mi : products) {
            sb.append(mi.getTitle()).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public String toString() {
        return title;
    }
}
