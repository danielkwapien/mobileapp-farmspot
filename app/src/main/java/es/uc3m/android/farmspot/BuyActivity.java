package es.uc3m.android.farmspot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class BuyActivity extends AppCompatActivity {

    TextView title, price, location, category, seller;
    ImageView image;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_buyproduct);

        HomeCardElement data = (HomeCardElement) getIntent().getSerializableExtra("data");

        title = findViewById(R.id.buyTitle);
        price = findViewById(R.id.buyPrice);
        location = findViewById(R.id.buyLocation);
        category = findViewById(R.id.buyCategory);
        image = findViewById(R.id.buyImage);
        seller = findViewById(R.id.sellerName);

        if (data.getImageUrl() != null) {
            Glide.with(image.getContext()) // Get a Glide instance
                    .load(data.getImageUrl()) // Load from the URL
                    .placeholder(R.drawable.profile_sample) // Optional: Placeholder image
                    .error(R.drawable.undraw_flowers_vx06) // Optional: Image to display on error
                    .into(image); // Load the image into the ImageView
        }
        title.setText(data.getTitle());
        seller.setText("Sold by: " + data.getSeller());
        price.setText(data.getPrice() + "€ / " + data.getUnit());
        location.setText(data.getLocation());
        category.setText(data.getCategory());


    }
}