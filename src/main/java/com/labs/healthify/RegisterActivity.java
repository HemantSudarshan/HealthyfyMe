package com.labs.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.labs.healthify.databinding.ActivityRegisterBinding;
import com.labs.healthify.repository.HealthifyRepository;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this , LoginActivity.class ));
            }
        });
        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.etregname.getText().toString();
                String email = binding.etmail.getText().toString();
                String pass = binding.etregpass.getText().toString();
                String conpass = binding.etconpass.getText().toString();

                if (username.length()==0 || pass.length()==0 || email.length()==0 || conpass.length()==0){
                    Toast.makeText(getApplicationContext(),getString(R.string.fill_details),Toast.LENGTH_SHORT).show();
                }else{
                    if(pass.compareTo(conpass)==0){
                        if(isvalid(pass)){
                            HealthifyRepository repository = new HealthifyRepository(getApplicationContext());
                            repository.register(username, email, pass, new HealthifyRepository.RegistrationCallback() {
                                @Override
                                public void onSuccess() {
                                    runOnUiThread(() -> {
                                        Toast.makeText(getApplicationContext(),getString(R.string.registered_successfully),Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this , LoginActivity.class));
                                    });
                                }

                                @Override
                                public void onError(String error) {
                                    runOnUiThread(() -> {
                                        Toast.makeText(getApplicationContext(),getString(R.string.registration_failed, error),Toast.LENGTH_SHORT).show();
                                    });
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(),getString(R.string.password_criteria_error),Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),getString(R.string.password_mismatch),Toast.LENGTH_SHORT).show();
                    }
            }}
        });
    }
    public static boolean isvalid(String passwordhere){
        int f1=0,f2=0,f3=0;
        if (passwordhere.length()<8){
            return false;
        }else{
            for(int p=0;p<passwordhere.length();p++){
                if (Character.isLetter((passwordhere.charAt(p)))){
                    f1=1;
                }
            }
            for(int r=0;r<passwordhere.length();r++){
                if (Character.isDigit(passwordhere.charAt(r))){
                    f2=1;
                }
            }
            for(int s=0;s<passwordhere.length();s++){
                char c = passwordhere.charAt(s);
                if (c>=33 &&c<=46 || c==64){
                    f3=1;
                }
            }
            if(f1==1 && f2==1 && f3==1 )
                return true;
            return false;
        }

    }
}