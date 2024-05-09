package es.uc3m.android.farmspot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import es.uc3m.android.farmspot.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements ItemListener {

    private ActivityMainBinding binding;

    private static final String ACTION_PRODUCT_ADDED = "es.uc3m.android.farmspot.product_added";
    private static final String ACTION_PRODUCT_SOLD = "es.uc3m.android.farmspot.product_sold";

    private BroadcastReceiver productAddedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            readDataFromFirestore();
        }
    };

    private BroadcastReceiver productSoldReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            readDataFromFirestore();
        }
    };

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_main);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                productAddedReceiver, new IntentFilter(ACTION_PRODUCT_ADDED));

        LocalBroadcastManager.getInstance(this).registerReceiver(
                productSoldReceiver, new IntentFilter(ACTION_PRODUCT_SOLD));

        progressBar = findViewById(R.id.progressBar);

        readDataFromFirestore();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home){
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
            }
        });


    }

    //private List<HomeCardElement> generateData() {
    //    List<HomeCardElement> data = new ArrayList<>();
    //    data.add(new HomeCardElement("White potato", "0,94 €/kg", "Chinchón, Madrid", "Vegetables, Potato", R.drawable.potato));
    //    data.add(new HomeCardElement("Galician blonde cow", "1.500 €/unit", "Abeledo, A Coruña", "Animal, Cow", R.drawable.cow));
    //    data.add(new HomeCardElement("Hard red wheat", "0,37 €/kg", "Villacastín, Segovia", "Cereal, Wheat", R.drawable.wheat));
    //    data.add(new HomeCardElement("Apple tree seeds", "0,39 €/g", "Navaluenga, Ávila", "Seed, fruit", R.drawable.apple_seeds));
    //    return data;
    //}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the receiver when the activity is destroyed
        LocalBroadcastManager.getInstance(this).unregisterReceiver(productAddedReceiver);
    }

    public void readDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        progressBar.setVisibility(View.VISIBLE);

        db.collection("product")
                .whereEqualTo("sold", false)
                .orderBy("dateAdded", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<HomeCardElement> data = new ArrayList<>();
                        AtomicInteger counter = new AtomicInteger(); // Counter for Firestore calls
                        int docCount = task.getResult().size(); // Number of documents

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            HomeCardElement product = document.toObject(HomeCardElement.class);
                            product.setProductId(document.getId());
                            //Log.d("Main activity", document.getId() + " => " + document.getData());
                            String sellerUserId = product.getUserId(); // Assuming you have this in HomeCardElement

                            db.collection("users").document(sellerUserId).get()
                                    .addOnSuccessListener(userDoc -> {
                                        String sellerUsername = userDoc.getString("username");
                                        product.setSeller(sellerUsername); // Assuming you add this setter
                                        data.add(product); // Now product has the username

                                        if (counter.incrementAndGet() == docCount) { // All Firestore calls completed
                                            progressBar.setVisibility(View.GONE);
                                            updateUI(data); // Update UI after all Firestore calls
                                        }
                                    });
                        }
                    } else {
                        Log.e("Firestore Read Error", String.valueOf(task.getException()));
                        // Handle the error
                    }
                });
    }

    private void updateUI(List<HomeCardElement> data) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new HomeRecyclerViewAdapter(data, this, this));
    }

    @Override
    public void onItemClick(HomeCardElement item) {
        Intent intent = new Intent(this, BuyActivity.class);
        intent.putExtra("data", item);
        startActivity(intent);
    }
}