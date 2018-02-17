package com.getirgotur.YemekEkle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.getirgotur.R;

/**
 * Created by halilmac on 17/02/2018.
 */

public class YemekEkleActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yemek_ekle);




        /*
        *public static final int PICK_IMAGE = 1;

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data)
{
    if (requestCode == PICK_IMAGE) {
        //TODO: action
    }
}
        * Intent intent = new Intent();
intent.setType("image/*");
intent.setAction(Intent.ACTION_GET_CONTENT);
startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        *
        * */
    }
}
