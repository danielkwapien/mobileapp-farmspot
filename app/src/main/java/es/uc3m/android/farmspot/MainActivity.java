package es.uc3m.android.farmspot;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

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

public class MainActivity extends AppCompatActivity {

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
        recyclerView.setAdapter(new HomeRecyclerViewAdapter(generateData(), this));

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
        data.add(new HomeCardElement("Card 1", "Description 1"));
        data.add(new HomeCardElement("Card 2", "Description 2"));
        data.add(new HomeCardElement("Card 3", "Description 3"));
        data.add(new HomeCardElement("Card 4", "Description 4"));
        data.add(new HomeCardElement("Card 5", "Description 5"));
        return data;
    }

}