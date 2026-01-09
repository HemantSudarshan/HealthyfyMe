package com.labs.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.labs.healthify.repository.HealthifyRepository;

public class LoginActivity extends AppCompatActivity {
    EditText edusername , edpass;
    Button btn;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edusername = findViewById(R.id.etlogname);
        edpass = findViewById(R.id.etlogpass);
        btn = findViewById(R.id.buttonlogin);
        tv = findViewById(R.id.tvreg);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edusername.getText().toString();
                String pass = edpass.getText().toString();

                if (username.length()==0 || pass.length()==0){
                    Toast.makeText(getApplicationContext(),getString(R.string.fill_details),Toast.LENGTH_SHORT).show();
                }else{
                    HealthifyRepository repository = new HealthifyRepository(getApplicationContext());
                    repository.login(username, pass, new HealthifyRepository.LoginCallback() {
                        @Override
                        public void onSuccess() {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(),getString(R.string.login_success),Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username",username);
                                editor.apply();
                                startActivity(new Intent(LoginActivity.this , HomeActivity.class));
                            });
                        }

                        @Override
                        public void onError(String error) {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(),getString(R.string.invalid_credentials),Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
            }}
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , RegisterActivity.class ));
            }
        });
    }
}