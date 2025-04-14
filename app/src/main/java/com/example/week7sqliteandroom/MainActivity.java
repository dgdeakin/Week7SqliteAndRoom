package com.example.week7sqliteandroom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity {


    EditText usernameEditText, passwordEditText;
    Button loginButton, signupButton, changePasswordButton, showAllUsersButton;
    AppDatabase db;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "user-database").allowMainThreadQueries().build();

        userDao = db.userDao();

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        showAllUsersButton = findViewById(R.id.showAllUsersButton);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if(username.isEmpty() || password.isEmpty())
            {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_LONG).show();
                return;
            }

            User user = userDao.login(username, password);

            if(user != null){
                Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_LONG).show();
            }
        });

        signupButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if(username.isEmpty() || password.isEmpty())
            {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_LONG).show();
            }

            User existingUser = userDao.findByUsername(username);

            if(existingUser != null){
                Toast.makeText(this, "Username already exists", Toast.LENGTH_LONG).show();
            }

            User newUser = new User(username, password);
            userDao.insert(newUser);

            Toast.makeText(this, "Signup Successful", Toast.LENGTH_LONG).show();


        });

        changePasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        showAllUsersButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShowUsersActivity.class);
            startActivity(intent);
        });



    }
}