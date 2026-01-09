package com.labs.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.labs.healthify.databinding.ActivityCartLabBinding;
import com.labs.healthify.repository.HealthifyRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CartLabActivity extends AppCompatActivity {
    private ActivityCartLabBinding binding;

    HashMap<String, String> item;
    ArrayList list;
    SimpleAdapter sa;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private String[][] packages={};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartLabBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username =  sharedPreferences.getString("username","").toString();

        HealthifyRepository repository = new HealthifyRepository(getApplicationContext());
        repository.getCartData(username, "lab", new HealthifyRepository.CartDataCallback() {
            @Override
            public void onSuccess(ArrayList dbData) {
                runOnUiThread(() -> {
                    float totalAmount = 0;
                    packages = new String[dbData.size()][];
                    for (int i=0;i<packages.length;i++){
                        packages[i]=new String[5];
                    }

                    for (int i=0;i<dbData.size();i++){
                        String arrData = dbData.get(i).toString();
                        String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
                        packages[i][0]=strData[0];
                        packages[i][4]="Cost : "+strData[1]+"/-";
                        totalAmount=totalAmount+Float.parseFloat(strData[1]);
                    }

                    binding.tvtotcost.setText("Total Cost : "+totalAmount);

                    list = new ArrayList();
                    for (int i =0; i<packages.length;i++){
                        item = new HashMap<String,String>();
                        item.put("line1",packages[i][0]);
                        item.put("line2",packages[i][1]);
                        item.put("line3",packages[i][2]);
                        item.put("line4",packages[i][3]);
                        item.put("line5",packages[i][4]);
                        list.add(item);
                    }

                    sa = new SimpleAdapter(CartLabActivity.this,list,R.layout.multi_lines,new String[] {"line1","line2","line3","line4","line5"},
                            new int[] {R.id.line_a,R.id.line_b,R.id.line_c,R.id.line_d,R.id.line_e});
                    binding.listViewcartitems.setAdapter(sa);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    binding.tvtotcost.setText("Total Cost : 0");
                });
            }
        });

        binding.buttoncartBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartLabActivity.this, LabTestActivity.class));
            }
        });

        binding.btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(CartLabActivity.this,LabTestBookActivity.class);
                it.putExtra("price",binding.tvtotcost.getText());
                it.putExtra("date",binding.btnseldeldate.getText());
                it.putExtra("time",binding.btnseldeltime.getText());
                startActivity(it);
            }
        });

        //datepicker
        initDatePicker();
        binding.btnseldeldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        //timepicker
        initTimePicker();
        binding.btnseldeltime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });

    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1 + 1;
                binding.btnseldeldate.setText(i2 + "/" + i1 + "/" + i);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis() + 86400000);
    }

    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                binding.btnseldeltime.setText(i + ":" + i1);
            }
        };
        Calendar cal = Calendar.getInstance();
        int hrs = cal.get(Calendar.HOUR);
        int mins = cal.get(Calendar.MINUTE);


        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;
        timePickerDialog = new TimePickerDialog(this, style, timeSetListener, hrs, mins, false);

    }
}