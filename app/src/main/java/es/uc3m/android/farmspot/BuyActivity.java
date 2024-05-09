package es.uc3m.android.farmspot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Objects;

public class BuyActivity extends AppCompatActivity {

    TextView title, price, location, category, seller;
    EditText quantityEditText;
    ImageView image;
    int quantity;
    double totalPrice;
    private static final String ACTION_PRODUCT_SOLD = "es.uc3m.android.farmspot.product_sold";


    @SuppressLint({"MissingInflatedId", "SetTextI18n", "DefaultLocale"})
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
        quantityEditText = findViewById(R.id.quantityEditText);

        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not required for this implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateTotalPrice(data);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        if (data.getImageUrl() != null) {
            Glide.with(image.getContext()) // Get a Glide instance
                    .load(data.getImageUrl()) // Load from the URL
                    .placeholder(R.drawable.add_picture_product) // Optional: Placeholder image
                    .error(R.drawable.add_picture_product) // Optional: Image to display on error
                    .into(image); // Load the image into the ImageView
        }

        int quantity = 1;
        double totalPrice = data.getPrice();
        try{
            quantity = Integer.parseInt(quantityEditText.getText().toString().trim());
            totalPrice = data.getPrice() * quantity;
        } catch (NumberFormatException ignored){

        }



        title.setText(data.getTitle());
        seller.setText("Sold by: " + data.getSeller());
        price.setText(String.format("%.2f", totalPrice) + "€ in total");
        // location.setText(data.getLatitude());
        category.setText(data.getCategory());





        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String sellerId = data.getUserId();
        String buyerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String productId = data.getProductId();

        if (sellerId.equals(buyerId)) {
            findViewById(R.id.BuyProduct).setEnabled(false); // Disable the button
            findViewById(R.id.warningBuyer).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.BuyProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sellerId.equals(buyerId)) {
                    Toast.makeText(BuyActivity.this, "You cannot buy your own product", Toast.LENGTH_SHORT).show();

                    return; // Exit the onClick to prevent purchase
                }

                db.collection("sales")
                        .add(new HashMap<String, Object>() {{
                            put("sellerId", sellerId);
                            put("buyerId", buyerId);
                            put("productId", productId);
                        }})
                        .addOnSuccessListener(documentReference -> {
                            db.collection("product")
                                    .document(productId)
                                    .update("sold", true)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(BuyActivity.this, "Purchase completed", Toast.LENGTH_SHORT).show();
                                        Intent broadcastIntent = new Intent(ACTION_PRODUCT_SOLD);
                                        LocalBroadcastManager.getInstance(BuyActivity.this).sendBroadcast(broadcastIntent);
                                        openMainActivity();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(BuyActivity.this, "Failed to complete the purchase", Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(BuyActivity.this, "Failed to complete the purchase", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        // Initialize bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            // Handle navigation item selection
            if (item.getItemId() == R.id.navigation_home){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.navigation_search){
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.navigation_add){
                startActivity(new Intent(getApplicationContext(), AddActivity.class));
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.navigation_favorites){
                startActivity(new Intent(getApplicationContext(), PurchasesActivity.class));
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.navigation_profile){
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void calculateTotalPrice(HomeCardElement data) {
        try {
            quantity = Integer.parseInt(quantityEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            quantity = 1;  // Set default quantity if parsing fails
        }
        totalPrice = data.getPrice() * quantity;
        price.setText(String.format("%.2f", totalPrice) + "€ in total");
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
