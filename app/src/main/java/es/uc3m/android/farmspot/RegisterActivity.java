package es.uc3m.android.farmspot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button registerButton;

    TextView signupTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_register);

        username = findViewById(R.id.registerUsername);
        password = findViewById(R.id.registerPassword);
        registerButton = findViewById(R.id.registerButton);
        signupTextView = findViewById(R.id.signupTextView);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().trim().equals("user") && password.getText().toString().trim().equals("1234")){
                    Toast.makeText(RegisterActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    openMainActivity();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}