package com.getirgotur.Giris;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.getirgotur.MainActivity;
import com.getirgotur.R;
import com.getirgotur.Kullanici;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by halilmac on 17/02/2018.
 */

public class GirisActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String TAG = "GirisActivity";
    private Kullanici kullanici;
    //AUTH
    public FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private String userName = "", userImage = "" , userGender = "-";
    private Fragment fragment;
    private int hangiSayfa = 0;


    //FİREBASE

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        if(savedInstanceState == null){
            fragmentDegistir(0);
        }
    }

    public void fragmentDegistir(int fragmentSecim){
        hangiSayfa = fragmentSecim;
        Bundle bundle;
        switch (fragmentSecim){
            case 0:
                fragment = new SplashFragment();
                break;
            case 1:
                fragment = new GirisFragment();
                bundle = new Bundle();
                fragment.setArguments(bundle);

                break;


        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

    }


    public void giris(final String kullaniciAdi, final String Konum){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new SplashFragment())
                .commit();
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            boolean isSignedIn = (user != null);
                            if (isSignedIn) {
                                kullanici =  new Kullanici();
                                kullanici.setId(user.getUid());

                                if(user.isAnonymous()){
                                    kullanici.setAdi(kullaniciAdi);
                                    fragmentDegistir(1);
                                    firebaseIslemleri(user);

                                }


                            }else{
                                fragmentDegistir(1);



                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(GirisActivity.this, "Giriş Başarısız.",
                                    Toast.LENGTH_SHORT).show();
                            fragmentDegistir(1);
                        }

                        Intent intent =  new Intent(GirisActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }



    private void firebaseIslemleri(FirebaseUser firebaseUser){

       

    }



}
