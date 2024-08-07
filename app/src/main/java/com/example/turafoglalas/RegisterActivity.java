package com.example.turafoglalas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();
    EditText usernameET;
    EditText emailET;
    EditText passwordET;
    EditText passwordRepeatET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 160921){
            finish();
        }

        usernameET = findViewById(R.id.editTextUserName);
        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);
        passwordRepeatET = findViewById(R.id.editTextPasswordRepeat);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        usernameET.setText(username);
        passwordET.setText(password);

        mAuth = FirebaseAuth.getInstance();

    }

    public void register(View view) {
        Button registerButton = findViewById(R.id.registerButton);
        Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.fadein);
        registerButton.startAnimation(animation);

        String username = usernameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordRepeat = passwordRepeatET.getText().toString();

        if(email.isEmpty() || password.isEmpty() || username.isEmpty() || passwordRepeat.isEmpty()){
            return;
        }

        if (!password.equals(passwordRepeat)){
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startTuraBrowsing();
                } else{
                    Toast.makeText(RegisterActivity.this, "User was not created: "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void backToLogin(View view) {
        Button alreadyHaveAnAccountButton = findViewById(R.id.alreadyHaveAnAccountButton);
        Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.fadein);
        alreadyHaveAnAccountButton.startAnimation(animation);

        finish();
    }

    private void startTuraBrowsing(){
        Intent intent = new Intent(this, TuraListActivity.class);
        startActivity(intent);
    }
}