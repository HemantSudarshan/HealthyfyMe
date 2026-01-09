package com.labs.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.labs.healthify.databinding.ActivityOrderDetailsBinding;
import com.labs.healthify.repository.HealthifyRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDetailsActivity extends AppCompatActivity {
    private ActivityOrderDetailsBinding binding;

    private String[][] order_detail = {};

    HashMap<String,String> item;
    ArrayList list;
    SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonODBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderDetailsActivity.this,HomeActivity.class));
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","").toString();
        
        HealthifyRepository repository = new HealthifyRepository(getApplicationContext());
        repository.getOrderData(username, new HealthifyRepository.OrderDataCallback() {
            @Override
            public void onSuccess(ArrayList dbData) {
                runOnUiThread(() -> {
                    order_detail = new String[dbData.size()][];
                    for (int i=0;i<order_detail.length;i++){
                        order_detail[i]=new String[5];
                        String arrData = dbData.get(i).toString();
                        String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
                        order_detail[i][0]=strData[0];
                        order_detail[i][1]=strData[1];//+" "+strData[3];

                        if (strData[7].compareTo("medicine")==0){
                            order_detail[i][3] = "Del:"+strData[4];
                        }else {
                            order_detail[i][3]="Del:"+strData[4]+" "+strData[5];
                        }
                        order_detail[i][2]="Rs."+strData[6];
                        order_detail[i][4]=strData[7];
                    }

                    list = new ArrayList();
                    for (int i =0;i<order_detail.length;i++){
                        item=new HashMap<String,String>();
                        item.put("line1",order_detail[i][0]);
                        item.put("line2",order_detail[i][1]);
                        item.put("line3",order_detail[i][2]);
                        item.put("line4",order_detail[i][3]);
                        item.put("line5",order_detail[i][4]);
                        list.add(item);
                    }

                    sa = new SimpleAdapter(OrderDetailsActivity.this,list,
                            R.layout.multi_lines,
                            new String[]{"line1","line2","line3","line4","line5"},
                            new int[]{R.id.line_a,R.id.line_b,R.id.line_c,R.id.line_d,R.id.line_e});
                    binding.listViewOD.setAdapter(sa);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    // Handle error - empty list
                    list = new ArrayList();
                    sa = new SimpleAdapter(OrderDetailsActivity.this,list,
                            R.layout.multi_lines,
                            new String[]{"line1","line2","line3","line4","line5"},
                            new int[]{R.id.line_a,R.id.line_b,R.id.line_c,R.id.line_d,R.id.line_e});
                    binding.listViewOD.setAdapter(sa);
                });
            }
        });
    }
}