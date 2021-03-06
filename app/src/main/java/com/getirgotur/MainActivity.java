package com.getirgotur;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.getirgotur.AnaSayfa.AnaSayfaFragment;
import com.getirgotur.Giris.GirisActivity;
import com.getirgotur.Giris.SplashFragment;
import com.getirgotur.Servisler.SiparisCanliTakipServis;
import com.getirgotur.Siparisler.SiparislerFragment;
import com.getirgotur.YemekEkle.YemekEkleActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public FirebaseAuth mAuth;
    private Kullanici kullanici;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        if(getIntent().getSerializableExtra("kullanici") != null){
            kullanici = (Kullanici) getIntent().getSerializableExtra("kullanici");
        }else{
            Intent intent =  new Intent(MainActivity.this,GirisActivity.class);
            startActivity(intent);
            finish();
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this, YemekEkleActivity.class);
                intent.putExtra("kullanici",kullanici);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View hView =  navigationView.getHeaderView(0);

        ImageView ivProfil = (ImageView)hView.findViewById(R.id.iv_profil);

        //Kullanıcı Profil Resmi
        String url =  kullanici.getResimUrl();
        Picasso picasso = Picasso.with(this);
        //Kullanıcının resmi yoksa bos resim yüklenecek
        loadImage(ivProfil, url != null ? picasso.load(url) : picasso.load(R.drawable.bos_kullanici));




        TextView tvKullaniciAdi = (TextView)hView.findViewById(R.id.tv_kullanici_adi);
        tvKullaniciAdi.setText(kullanici.getAdi().toUpperCase());


        Fragment fragment = new AnaSayfaFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("kullanici",kullanici);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container,fragment)
                .commit();



        startService(new Intent(this, SiparisCanliTakipServis.class));
        //startService(new Intent(this, SiparisCanliTakipServis.class));
    }

    private void loadImage(ImageView iv, RequestCreator rc){
        rc.error(R.drawable.bos_kullanici)
                .transform(new CircleTransform())
                .memoryPolicy (MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(iv);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        if (id == R.id.nav_home) {

            fragment = new AnaSayfaFragment();
        } else if (id == R.id.nav_location) {

        } else if (id == R.id.nav_sattiklarin) {
            fragment = new SiparislerFragment();
            bundle.putString("islem","Satilanlar");

        }  else if (id == R.id.nav_aldiklarim) {
            fragment = new SiparislerFragment();
            bundle.putString("islem","Alilanlar");

        } else if (id == R.id.nav_exit) {
            cikis();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if(fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();

            bundle.putSerializable("kullanici",kullanici);
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }

        return true;
    }


    public void cikis() {
        mAuth.signOut();
        Intent intent =  new Intent(MainActivity.this,GirisActivity.class);
        startActivity(intent);
        finish();
    }
}
