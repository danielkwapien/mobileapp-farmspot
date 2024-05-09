package es.uc3m.android.farmspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class PurchasesActivity extends AppCompatActivity implements ItemListener{

    private ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_purchases);

        progressBar = findViewById(R.id.progressBar);

        fetchPurchase();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_purchases);

        // Set listener for bottom navigation view item selection
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                else if (item.getItemId() == R.id.navigation_purchases){
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

    @Override
    public void onItemClick(HomeCardElement item) {
        Intent intent = new Intent(this, BuyActivity.class);
        intent.putExtra("data", item);
        startActivity(intent);
    }

    private void fetchPurchase(){
        progressBar.setVisibility(View.VISIBLE);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("sales")
                .whereEqualTo("buyerId", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        List<String> productIds = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String productId = document.getString("productId");
                            productIds.add(productId);
                        }
                        fetchProductData(productIds);
                        findViewById(R.id.purchasesTitle).setVisibility(productIds.isEmpty() ? View.GONE : View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void fetchProductData(List<String> productIds){
        List<HomeCardElement> data = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(productIds.size());
        for (String productId : productIds){

            db.collection("product")
                    .document(productId)
                    .get()
                    .addOnSuccessListener(document -> {
                        HomeCardElement item = document.toObject(HomeCardElement.class);
                        if (item != null){
                            data.add(item);
                        }
                        if (counter.decrementAndGet() == 0) { // Completed all fetches
                            progressBar.setVisibility(View.GONE);
                            findViewById(R.id.noDisplayText).setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE); // Check for empty data
                            updateUI(data);
                        }
                    });
        }
    }

    private void updateUI(List<HomeCardElement> data) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new HomeRecyclerViewAdapter(data, this, this));
    }
}