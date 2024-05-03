package es.uc3m.android.farmspot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class BuyActivity extends AppCompatActivity {

    TextView title, price, location, category;
    ImageView image;


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

        title.setText(data.getTitle());
        price.setText(data.getPrice());
        location.setText(data.getLocation());
        category.setText(data.getCategory());


    }
}