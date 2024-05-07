package es.uc3m.android.farmspot;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresExtension;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AddActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ImageView imageViewProduct;
    private Button buttonUploadImage;
    private static final int PICK_IMAGE_REQUEST = 100;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    ActivityResultLauncher<Intent> resultLauncher;

    private static final String ACTION_PRODUCT_ADDED = "es.uc3m.android.farmspot.product_added";

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
        EditText locationEditText = findViewById(R.id.locationEditText);
        imageViewProduct = findViewById(R.id.imageViewProduct);


        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        findViewById(R.id.buttonUploadImage).setOnClickListener(view -> openImageChooser());

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            imageUri = data.getData();
                            imageViewProduct.setImageURI(imageUri);
                        }
                    }
                });

        findViewById(R.id.AddProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String category = categoryEditText.getText().toString();
                String price = priceEditText.getText().toString();
                String unit = unitEditText.getText().toString();
                String location = locationEditText.getText().toString();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if (title.isEmpty() || description.isEmpty() || category.isEmpty() || price.isEmpty() || location.isEmpty() || unit.isEmpty()) {
                    Toast.makeText(AddActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double[] coordinates = convertLocationToCoordinates(location);

                Map<String, Object> product = new HashMap<>();
                product.put("title", title);
                product.put("description", description);
                product.put("category", category);
                product.put("price", price);
                product.put("unit", unit);
                product.put("userId", userId);
                product.put("latitude", coordinates[0]);
                product.put("longitude", coordinates[1]);

                if (imageUri != null) {
                    // Create a reference to the image
                    StorageReference imageRef = storageRef.child(UUID.randomUUID().toString());

                    // Upload the image
                    imageRef.putFile(imageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                // Get image download URL
                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                    // Add image url to the product Map
                                    product.put("imageUrl", uri.toString());
                                    db.collection("product")
                                            .add(product)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Intent broadcastIntent = new Intent(ACTION_PRODUCT_ADDED);
                                                    LocalBroadcastManager.getInstance(AddActivity.this).sendBroadcast(broadcastIntent);

                                                    Toast.makeText(AddActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AddActivity.this, "Failed to upload the product", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(AddActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                            });
                }

                openMainActivity();
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

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        resultLauncher.launch(intent);  // Use the resultLauncher to launch the intent
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private double[] convertLocationToCoordinates(String location) {
        // Initialize Geocoder
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        double[] coordinates = new double[2];

        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);

            if (!addresses.isEmpty()) {

                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                coordinates[0] = latitude;
                coordinates[1] = longitude;
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coordinates;
    }
}
