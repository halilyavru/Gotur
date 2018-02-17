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
import com.getirgotur.Yemek;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by halilmac on 17/02/2018.
 */

public class GirisActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String TAG = "GirisActivity";
    private Kullanici kullanici ;
    //AUTH
    public FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private String userName = "", userImage = "" , userGender = "-";
    private Fragment fragment;
    private int hangiSayfa = 0;


    //FİREBASE
    private ValueEventListener valueEventListener;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        mAuth = FirebaseAuth.getInstance();
        checkLogin();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

    }

    //Fragmentleri yönettiğimiz method
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

    //Kullanıcı Adı ve Haritadan konum seçildikten sonra firebaseye login oluyoruz.
    public void giris(final String kullaniciAdi, final String konum){
        kullanici = new Kullanici();
        kullanici.setAdi(kullaniciAdi);
        kullanici.setKonum(konum);
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
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(GirisActivity.this, "Giriş Başarısız.",
                                    Toast.LENGTH_SHORT).show();
                            fragmentDegistir(1);
                        }


                    }
                });
    }


    private void updateUI(FirebaseUser user){
        boolean isSignedIn = (user != null);
        System.out.println("Giriş : "+isSignedIn);
        if (isSignedIn) {

            if(kullanici == null){
                kullanici =  new Kullanici();
            }

            kullanici.setId(user.getUid());

            if(user.isAnonymous()){
                firebaseIslemleri(user);
            }


        }else{
            fragmentDegistir(1);
        }
    }



    private void firebaseIslemleri(FirebaseUser firebaseUser){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(firebaseUser.getUid());

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Kullanıcı yoksa kullanıcıyı ekliyoruz
                if(dataSnapshot.getValue() == null){
                    Map<String,String> mapKullanici =  new HashMap<>();
                    mapKullanici.put("adi",kullanici.getAdi());
                    mapKullanici.put("konum",kullanici.getKonum());
                    databaseReference.setValue(mapKullanici);

                }else{//Kullanıcı varsa bilgilerini aldıktan sonra ana sayfaya yonlendiriyoruz
                    kullanici.setAdi(dataSnapshot.child("adi").getValue().toString());
                    kullanici.setKonum(dataSnapshot.child("konum").getValue().toString());
                    if(dataSnapshot.child("yemekler").getValue() != null){
                        String [] arrayYemekler = dataSnapshot.child("yemekler").getValue().toString().split(",");
                        kullanici.setListYemeklerim(Arrays.asList(arrayYemekler));
                    }
                    if(dataSnapshot.child("resimUrl").getValue() != null){
                        kullanici.setResimUrl(dataSnapshot.child("resimUrl").getValue().toString());
                    }
                    if(dataSnapshot.child("maxYemekGoturmeMesafesi").getValue() != null){
                        kullanici.setMaxYemekGoturmeMesafesi(Integer.parseInt(dataSnapshot.child("maxYemekGoturmeMesafesi").getValue().toString()));
                    }


                    databaseReference.removeEventListener(valueEventListener);
                    Intent intent =  new Intent(GirisActivity.this,MainActivity.class);
                    intent.putExtra("kullanici",kullanici);
                    startActivity(intent);
                    finish();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                databaseReference.removeEventListener(valueEventListener);
                fragmentDegistir(1);

            }
        };
        databaseReference.addValueEventListener(valueEventListener);

    }

    public void checkLogin(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }



}
