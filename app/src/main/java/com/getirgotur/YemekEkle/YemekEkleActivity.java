package com.getirgotur.YemekEkle;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getirgotur.Giris.GirisActivity;
import com.getirgotur.Kullanici;
import com.getirgotur.MainActivity;
import com.getirgotur.R;
import com.getirgotur.Yemek;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.wefika.flowlayout.FlowLayout;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Created by halilmac on 17/02/2018.
 */

public class YemekEkleActivity extends AppCompatActivity implements View.OnClickListener{

    private Kullanici kullanici;
    private FlowLayout flowLayout;
    private EditText etChipEkle,etFiyat;
    private Map<String,Boolean> mapChips = new HashMap<>();
    private int PICK_IMAGE_REQUEST = 101;
    private ImageView ivYemekResim;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Button btnYemekResimSec;
    private boolean etDurum = true;
    private Uri filePath = null;
    private final NumberFormat format = NumberFormat.getIntegerInstance(Locale.ITALIAN);
    private String [] arrayStok = new String[100];
    private int stok = 0;
    private TextView tvStok;
    private Map<String,String> mapYemekler ;
    private AutoCompleteTextView acYemek;

    //FİREBASE
    private ValueEventListener valueEventListener;
    private DatabaseReference databaseReference;
    private String [] arrayYemekler = {};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yemek_ekle);

        if(getIntent().getSerializableExtra("kullanici") != null){
            kullanici = (Kullanici) getIntent().getSerializableExtra("kullanici");
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        for (int i = 0; i < arrayStok.length; i++) {
            arrayStok[i] = (i+1)+"";
        }

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Yemek Kodları");
        acYemek = (AutoCompleteTextView) findViewById(R.id.ac_yemek_adi);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mapYemekler = new HashMap<>();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    mapYemekler.put(snapshot.getValue().toString(),snapshot.getKey().toString());
                }
                List<String> keys = new ArrayList<>(mapYemekler.keySet());
                arrayYemekler = new String[keys.size()];
                keys.toArray(arrayYemekler);


                ArrayAdapter<String> adapter = new ArrayAdapter<String> (YemekEkleActivity.this, android.R.layout.select_dialog_item, arrayYemekler);
                acYemek.setThreshold(1);
                acYemek.setAdapter(adapter);


                databaseReference.removeEventListener(valueEventListener);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                databaseReference.removeEventListener(valueEventListener);


            }
        };
        databaseReference.addValueEventListener(valueEventListener);

        btnYemekResimSec= (Button) findViewById(R.id.btn_resim_sec);
        ivYemekResim = (ImageView) findViewById(R.id.iv_yemek_resim);
        flowLayout = (FlowLayout) findViewById(R.id.flow_layout);

        etChipEkle = (EditText) findViewById(R.id.et_chip_ekle);
        tvStok = (TextView) findViewById(R.id.tv_stok);



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
                    etChipEkle.setError("En az 2 harf giriniz.");
                }


                FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams
                        (FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(6,6,6,6);

                flowLayout.addView(v,layoutParams);
            }
        });

        btnYemekResimSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resimSec();
            }
        });

        etFiyat = (EditText) findViewById(R.id.et_yemek_fiyat);
        etFiyat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etDurum){
                    etDurum = false;
                    String formatString = fortmatFiyat(s.toString());
                    etFiyat.setText(formatString);
                    etFiyat.setSelection(formatString.length());
                }else{
                    etDurum =true;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });


        findViewById(R.id.ll_stok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(YemekEkleActivity.this);
                builder.setCancelable(true);

                builder.setTitle(R.string.stok);
                int selectedPosition = -1;

                if(stok>0){
                    for (int i = 0; i < arrayStok.length; i++) {
                        if(Integer.parseInt(arrayStok[i]) == stok){
                            selectedPosition = i;
                            break;
                        }
                    }
                }

                builder.setSingleChoiceItems(arrayStok, selectedPosition, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        stok = Integer.parseInt(arrayStok[which]);
                        tvStok.setText(arrayStok[which]);
                        dialog.dismiss();
                    }

                });

                builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                    }
                });

                dialog = builder.create();
                dialog.show();
            }
        });

        //Girilen yemek verilerini kontrol edip firebaseye kaydediyoruz
        findViewById(R.id.btn_yemek_ekle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(YemekEkleActivity.this);
                builder.setTitle(R.string.uyari);

                String mesaj = "";

                if(filePath == null){
                    mesaj = "Resim Seçiniz";
                } else if (acYemek.getText() == null || acYemek.getText().toString().trim().length()<3) {
                    mesaj = "Yemek Adı En Az 3 Karakter Olmalı.";
                }else if(mapYemekler.get(acYemek.getText().toString()) == null){
                    mesaj = "Listede olan bir yemek seçiniz.";
                }else if (etFiyat.getText() == null || etFiyat.getText().toString().trim().length() == 0) {

                }else{

                    Yemek yemek =  new Yemek();
                    yemek.setSahipAdi(kullanici.getAdi());
                    yemek.setAdi(acYemek.getText().toString());
                    yemek.setSahipKonum(kullanici.getKonum());
                    yemek.setFiyat(etFiyat.getText().toString());
                    yemek.setId(mapYemekler.get(acYemek.getText().toString()));
                    yemek.setPuan(0.0);
                    yemek.setFiyatBirimi("TL");
                    yemek.setPuanlamaSayisi(0);
                    yemek.setSahipId(kullanici.getId());
                    yemek.setStok(stok);
                    yemek.setResimUrl("");

                    String yemeginIcindekiler = "";
                    for (String key : mapChips.keySet()) {

                        if(yemeginIcindekiler.trim().length()>0){
                            yemeginIcindekiler += ",";
                        }
                        yemeginIcindekiler+=key;

                    }
                    yemek.setMalzeme(yemeginIcindekiler);

                    Yemek [] arrayYemek = {yemek};
                    uploadImage(arrayYemek);

                }

                if (mesaj.trim().length() > 0) {
                    builder.setMessage(mesaj);
                    builder.setNegativeButton(R.string.tamam, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();
                }
               /* portfoy.setSatisFiyati(inputFiyat.getText().toString().trim().replace(".", ""));
                portfoy.setSatisFiyatBirimi(sp.getSelectedItem().toString());
                tvSatisFiyati.setText(fortmatFiyat(portfoy.getSatisFiyati())+" "+portfoy.getSatisFiyatBirimi());*/

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



    private void resimSec() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {

            filePath = data.getData();
            Picasso.with(YemekEkleActivity.this).load(filePath).into(ivYemekResim);
            btnYemekResimSec.setVisibility(View.GONE);
            ivYemekResim.setVisibility(View.VISIBLE);
        }
    }

    private void uploadImage( final Yemek [] arrayYemek) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Yemek Ekleniyor");
            progressDialog.setCancelable(false);
            progressDialog.show();


            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            arrayYemek[0].setResimUrl(taskSnapshot.getDownloadUrl().toString());
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Yemekler").child(mapYemekler.get(acYemek.getText().toString())).child(kullanici.getId());
                            databaseReference.setValue(arrayYemek[0]);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //Toast.makeText(YemekEkleActivity.this, "Resim Yüklendi.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(YemekEkleActivity.this, "Yükleme Başarısız. ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.setMessage("Yükleniyor...");
                        }
                    });
        }
    }

    private String fortmatFiyat(String str){
        String formatstr = "";
        if(str.trim().length()>0){
            formatstr = format.format(Long.parseLong(str.replace(".", "")));

        }

        return formatstr;
    }

}
