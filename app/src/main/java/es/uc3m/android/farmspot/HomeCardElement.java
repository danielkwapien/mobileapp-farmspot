package es.uc3m.android.farmspot;

import java.io.Serializable;

public class HomeCardElement implements Serializable {

    public String title;
    public String price;
    public String location;
    public String category;

    public int image;

    public HomeCardElement(String title, String price, String location, String category, int image) {
        this.image = image;
        this.title = title;
        this.price = price;
        this.location = location;
        this.category = category;
    }

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

    public int getImage() { return image; }

    public void setImage(int image) { this.image = image; }

}
