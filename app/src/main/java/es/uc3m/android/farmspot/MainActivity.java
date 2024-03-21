package es.uc3m.android.farmspot;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.uc3m.android.farmspot.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements ItemListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new HomeRecyclerViewAdapter(generateData(), this, this));



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

    private List<HomeCardElement> generateData() {
        List<HomeCardElement> data = new ArrayList<>();
        data.add(new HomeCardElement("White potato", "0,94 €/kg", "Chinchón, Madrid", "Vegetables, Potato", R.drawable.potato));
        data.add(new HomeCardElement("Galician blonde cow", "1.500 €/unit", "Abeledo, A Coruña", "Animal, Cow", R.drawable.cow));
        data.add(new HomeCardElement("Hard red wheat", "0,37 €/kg", "Villacastín, Segovia", "Cereal, Wheat", R.drawable.wheat));
        data.add(new HomeCardElement("Apple tree seeds", "0,39 €/g", "Navaluenga, Ávila", "Seed, fruit", R.drawable.apple_seeds));
        return data;
    }

    @Override
    public void onItemClick(HomeCardElement item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, BuyActivity.class);
        // intent.putExtra("title", item.getTitle());
        // intent.putExtra("price", item.getPrice());
        // intent.putExtra("location", item.getLocation());
        // intent.putExtra("category", item.getCategory());
        // intent.putExtra("image", item.getImage());
        // startActivity(intent);
    }


}