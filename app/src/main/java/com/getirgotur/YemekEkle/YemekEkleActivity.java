package com.getirgotur.YemekEkle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.getirgotur.R;
import com.wefika.flowlayout.FlowLayout;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by halilmac on 17/02/2018.
 */

public class YemekEkleActivity extends AppCompatActivity implements View.OnClickListener{

    private FlowLayout flowLayout;
    private EditText etChipEkle;
    private Map<String,Boolean> mapChips = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yemek_ekle);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        etChipEkle = (EditText) findViewById(R.id.et_chip_ekle);

        findViewById(R.id.btn_chip_ekle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View v = LayoutInflater.from(YemekEkleActivity.this).inflate(R.layout.item_chip, null);
                final TextView tvChip = (TextView) v.findViewById(R.id.tv_item_chip);

                //Yemeğin içinde kullanılan malzemelerin girişi
                if( etChipEkle.getText() != null && etChipEkle.getText().toString().trim().length()>1 ){
                    final String textChip = etChipEkle.getText().toString().trim();
                    tvChip.setText(etChipEkle.getText().toString().trim());
                    mapChips.put(textChip,true);
                    flowLayout.removeView(v);
                    etChipEkle.setText("");

                    v.findViewById(R.id.iv_chip_delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mapChips.remove(textChip);
                            flowLayout.removeView(v);
                            for (String key :
                                    mapChips.keySet()) {
                                System.out.println("Chip  = " + key);
                            }
                        }
                    });
                }else{
                    etChipEkle.setError("En az 2 harften giriniz.");
                }


                FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams
                        (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(6,6,6,6);

                flowLayout.addView(v,layoutParams);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
