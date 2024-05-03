package es.uc3m.android.farmspot;

import java.io.Serializable;

public class HomeCardElement implements Serializable {

    public String title;
    public String price;
    public String location;
    public String category;
    public String unit;

    //public int image;

    public HomeCardElement(String title, String price, String unit, String location, String category) {
        //this.image = image;
        this.title = title;
        this.price = price;
        this.unit = unit;
        this.location = location;
        this.category = category;
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

    //public int getImage() { return image; }

    //public void setImage(int image) { this.image = image; }

}
