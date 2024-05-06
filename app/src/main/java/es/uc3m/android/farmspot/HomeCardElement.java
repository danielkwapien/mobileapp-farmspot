package es.uc3m.android.farmspot;

import android.net.Uri;

import java.io.Serializable;
import java.util.UUID;

public class HomeCardElement implements Serializable {

    public String title;
    public String price;
    public String location;
    public String category;
    public String unit;
    public String imageUrl;
    public String seller;
    public String userId;

    //public int image;

    public HomeCardElement(String imageUrl, String title, String price, String unit, String location, String category) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.price = price;
        this.unit = unit;
        this.location = location;
        this.category = category;
        this.userId = userId;
        this.seller = seller;
    }

    public HomeCardElement() { }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String image) { this.imageUrl = image; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getSeller() { return seller; }

    public void setSeller(String seller) { this.seller = seller; }
}
