package es.uc3m.android.farmspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_add);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_add);

        EditText titleEditText = findViewById(R.id.titleAddProduct);
        EditText descriptionEditText = findViewById(R.id.descriptionEditText);
        EditText categoryEditText = findViewById(R.id.categoryEditText);
        EditText priceEditText = findViewById(R.id.priceEditText);
        EditText unitEditText = findViewById(R.id.unitEditText);



        db = FirebaseFirestore.getInstance();

        findViewById(R.id.AddProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String category = categoryEditText.getText().toString();
                String price = priceEditText.getText().toString();
                String unit = unitEditText.getText().toString();

                Map<String, Object> product = new HashMap<>();
                product.put("Title", title);
                product.put("Description", description);
                product.put("Category", category);
                product.put("Price", price);
                product.put("unit", unit);

                db.collection("product")
                        .add(product)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(AddActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddActivity.this, "Failed to upload the product", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_favorites){
                    startActivity(new Intent(getApplicationContext(), FavoritesActivity.class));
                    finish();
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_profile){
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

}