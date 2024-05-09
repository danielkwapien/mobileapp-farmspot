package es.uc3m.android.farmspot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private EditText nameEditText, addressEditText;
    private Button saveButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_settings);

        nameEditText = findViewById(R.id.name_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);
        saveButton = findViewById(R.id.save_button);
        db = FirebaseFirestore.getInstance();

        fetchUserData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserData();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                finish();
            }
        });
    }

    private void fetchUserData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userDocRef = db.collection("users").document(userId);

        userDocRef.get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        nameEditText.setText(document.getString("name"));
                        addressEditText.setText(document.getString("address"));
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SettingsActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUserData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userDocRef = db.collection("users").document(userId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", nameEditText.getText().toString());
        updates.put("address", addressEditText.getText().toString());

        userDocRef.update(updates)
                .addOnSuccessListener(unused -> {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userName", nameEditText.getText().toString());
                    editor.apply();

                    Toast.makeText(SettingsActivity.this, "Settings updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SettingsActivity.this, "Failed to update settings", Toast.LENGTH_SHORT).show();
                });
    }
}
