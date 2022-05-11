package com.erictoader.fooddelivery.model;

import java.io.Serializable;
import java.util.Objects;

public abstract class MenuItem implements Comparable<MenuItem>, Serializable {
    protected String title;
    protected Double rating;
    protected Integer calories;
    protected Integer protein;
    protected Integer fat;
    protected Integer sodium;
    protected Integer price;

    public abstract Integer computePrice();
    public abstract int compareTo(MenuItem mi);
    public abstract String toString();

    public abstract String getTitle();
    public abstract void setTitle(String title);
    public abstract Double getRating();
    public abstract void setRating(Double rating);
    public abstract Integer getCalories();
    public abstract void setCalories(Integer calories);
    public abstract Integer getProtein();
    public abstract void setProtein(Integer protein);
    public abstract Integer getFat();
    public abstract void setFat(Integer fat);
    public abstract Integer getSodium();
    public abstract void setSodium(Integer sodium);
    public abstract Integer getPrice();
    public abstract void setPrice(Integer price);
    public abstract String getContains();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        if(this.title == null) return false;
        return title.equals(menuItem.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
