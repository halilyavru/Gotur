package com.getirgotur.Servisler;

/**
 * Created by halilmac on 18/02/2018.
 */


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.getirgotur.R;
import com.getirgotur.Siparisler.AdapterSiparisler;
import com.getirgotur.Siparisler.Siparis;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SiparisCanliTakipServis extends Service {

    private List<Siparis> listSiparisler =new ArrayList<>();
    SharedPreferences sharedPref;
    RecyclerView rcCanliSiparisler;
    private WindowManager mWindowManager;
    private View mFloatingView;
    int height;
    int merkez;
    TextView tvKucultBuyut;
    TextView tvSistemiKapat,tvTumunuGoster;
    WindowManager.LayoutParams params;
    boolean kucuk = false;
    private DatabaseReference databaseReference;
    private AdapterSiparisler adapter;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {


        SharedPreferences preferences = getSharedPreferences("Bilgilerim",
                MODE_PRIVATE);
        //Canlı Olarak siparişlerin listelendiği görüntünün tüm görüntülerin üzerinde olması için windowmanagerin içine ekleyeceğiz
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        mFloatingView = LayoutInflater.from(SiparisCanliTakipServis.this).inflate(R.layout.servis_siparis_takip, null);
        rcCanliSiparisler=(RecyclerView)mFloatingView.findViewById(R.id.rcCanliSiparisler);
        tvSistemiKapat=(TextView)mFloatingView.findViewById(R.id.tvSistemikapat);
        tvTumunuGoster=(TextView)mFloatingView.findViewById(R.id.tvTumunuGoster);
        tvKucultBuyut=(TextView)mFloatingView.findViewById(R.id.tvKucultBuyut);


        tvSistemiKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kullanıcının dilediği zaman sistemi kapatabilemsini sağlar servisi sonlandorır
                SiparisCanliTakipServis.this.stopSelf();
                mWindowManager.removeView(mFloatingView);
            }
        });
        tvTumunuGoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //yeni activity de tüm siparişleri inceleme
                /*Intent dialogIntent = new Intent(SiparisCanliTakipServis.this, ImageUpload.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);*/
            }
        });
        tvKucultBuyut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kucuk){

                }else {

                }
            }
        });


        //oluşturduğumuz görüntüyü ekledik
        mWindowManager.addView(mFloatingView, params);


        //siparişlerin tek sıra ile goüntülenmesini sağlar
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcCanliSiparisler.setLayoutManager(mLayoutManager);
        rcCanliSiparisler.setItemAnimator(new DefaultItemAnimator());



        adapter = new AdapterSiparisler(getBaseContext(), listSiparisler , true, null);
        rcCanliSiparisler.setAdapter(adapter);


        Toast.makeText(this, "kullanici :"+preferences.getString("kullaniciId",""), Toast.LENGTH_SHORT).show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Satilanlar").child(preferences.getString("kullaniciId",""));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listSiparisler.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    Siparis siparis = snapshot.getValue(Siparis.class);
                    listSiparisler.add(siparis);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        tasimaGoreviAta();

        super.onCreate();
    }




    //Oluşturduğumuz görüntünün tut sürükle ile ekranın farklı konumlarını taşınmasını sağlıyor
    private void tasimaGoreviAta()
    {


        mFloatingView.findViewById(R.id.tasiyici).setOnTouchListener(new View.OnTouchListener() {

            private int dokunmaBaslangicX;
            private int dokunmaBaslangicY;
            private float objeDokunulanNoktaX;
            private float objeDokunulanNoktaY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:


                        //telefonun yatay yada dikey konumda olduğunu
                        int orientation=SiparisCanliTakipServis.this.getResources().getConfiguration().orientation;
                        if(orientation== Configuration.ORIENTATION_PORTRAIT){
                            merkez=mWindowManager.getDefaultDisplay().getWidth()/2-150;
                            height=mWindowManager.getDefaultDisplay().getHeight();
                            System.out.println("yon"+1);
                        }
                        else{
                            height=mWindowManager.getDefaultDisplay().getHeight();
                            merkez=mWindowManager.getDefaultDisplay().getWidth()/2-200;
                            System.out.println("yon"+2);
                        }




                        //dokunduğumuzda direk x ve y kordinatını alıyoruz kaydırırken hesaplama için kullanacağız
                        dokunmaBaslangicX = params.x;
                        dokunmaBaslangicY = params.y;

                        //get the touch location
                        objeDokunulanNoktaX = event.getRawX();
                        objeDokunulanNoktaY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_MOVE:

                        //x ve y kordinatını başlangıç durumu ile son durum farkını alarak n kadar hareket sağlandığını hesaplayacağım
                        int y = dokunmaBaslangicY + (int) (event.getRawY() - objeDokunulanNoktaY);
                        int x = dokunmaBaslangicX + (int) (event.getRawX() - objeDokunulanNoktaX);
                        params.x= x;
                        params.y= y;

                        //kaydırmanın gerçekleşmesi için görünümünü güncelledim
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;

                    case MotionEvent.ACTION_UP:

                        if (tiklandiMi(dokunmaBaslangicX,params.x,dokunmaBaslangicY,params.y)){
                            if (mFloatingView.findViewById(R.id.rlRcyler).getVisibility()==View.VISIBLE){
                                mFloatingView.findViewById(R.id.rlRcyler).setVisibility(View.GONE);
                                return true;
                            }
                            mFloatingView.findViewById(R.id.rlRcyler).setVisibility(View.VISIBLE);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    //
    private boolean tiklandiMi(float baslangicX, float bitisX, float baslangicY, float bitisY) {
        float farkX = Math.abs(baslangicX - bitisX);
        float farkY = Math.abs(baslangicY - bitisY);
        return !(farkX > 200 || farkY > 200);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Servis Başladı", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Servis Sonlandırıldı", Toast.LENGTH_LONG).show();
    }
}