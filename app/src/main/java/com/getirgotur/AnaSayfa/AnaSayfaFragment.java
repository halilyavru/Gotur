package com.getirgotur.AnaSayfa;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.getirgotur.Kullanici;
import com.getirgotur.R;
import com.getirgotur.Siparisler.Siparis;
import com.getirgotur.Yemek;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by halilmac on 18/02/2018.
 */

public class AnaSayfaFragment extends Fragment implements OnMapReadyCallback, LocationListener, OnMarkerClickListener{

    private LocationManager mLocationManager;
    private GoogleMap mMap;
    private LatLng[] arrayLatLang = {new LatLng(42, 26), new LatLng(36, 26), new LatLng(36, 45), new LatLng(42, 45)};
    private double currentLat = 0;
    private double currentLon = 0;
    private boolean mLocationPermissionGranted = false;
    private static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1453;
    private Marker marker;
    private List<Yemek> listYemek ;
    private RecyclerView recyclerView;
    private AdapterYemekler adapter;
    //FİREBASE

    private DatabaseReference databaseReference;
    private Kullanici kullanici;
    private HashMap<String,Integer> mesafeler =  new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);


                if(getArguments().getSerializable("kullanici") != null){
                    kullanici = (Kullanici) getArguments().getSerializable("kullanici");
                }


        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            getLocationPermission();

        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, AnaSayfaFragment.this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, AnaSayfaFragment.this);


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        listYemek = new ArrayList<>();
        adapter = new AdapterYemekler(getActivity(), listYemek , mesafeler);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.bosluk);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Yemek yemek = listYemek.get(position);
                siparisVerDialog(yemek);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Yemekler");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listYemek.clear();
                String [] konum2 = kullanici.getKonum().split(",");
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    for (DataSnapshot subSnapshot: snapshot.getChildren()) {
                        Yemek yemek = subSnapshot.getValue(Yemek.class);
                        String [] konum1 = yemek.getSahipKonum().split(",");
                        float [] mesafe = new float[1];
                                Location.distanceBetween(Double.parseDouble(konum1[0]),Double.parseDouble(konum1[1]),
                                Double.parseDouble(konum2[0]), Double.parseDouble(konum2[1]),mesafe);
                        System.out.println("Mesafe: "+mesafe[0]);
                        mesafeler.put(yemek.getSahipId(),(int)mesafe[0]/1000);
                        if(yemek.getStok()>0 && !yemek.getSahipId().equals(kullanici.getId())){
                            listYemek.add(yemek);
                        }
                        for (String key :
                                mesafeler.keySet()) {
                            System.out.println("key: "+key+", value: "+mesafeler.get(key));

                        }

                    }
                }


                for (String key:mesafeler.keySet()) {
                    //System.out.println("Mesafe "+key+" --  "+));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public  class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private AnaSayfaFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final AnaSayfaFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));

                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    private void siparisVerDialog(final Yemek yemek){

        final Siparis siparis =  new Siparis();
        siparis.setTarih(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
        siparis.setSaat(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
        siparis.setAliciId(kullanici.getId());
        siparis.setAliciAdi(kullanici.getAdi());
        siparis.setAliciKonum(kullanici.getKonum());
        siparis.setTeslimatDurumu(false);
        siparis.setSiparisKabul(false);

        AlertDialog alertDialog = null;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(yemek.getAdi());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_siparis, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.et_siparis_not);

        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.sp_siparis_miktari);

        final String [] siparisStok = new String[yemek.getStok()];

        for (int i = 0; i < siparisStok.length; i++) {
            siparisStok[i] = (i+1)+"";
        }

        final ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getContext(),R.layout.item_spinner, siparisStok);
        spinner.setAdapter(adapterSpinner);
        dialogBuilder.setNegativeButton(R.string.vazgec, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogBuilder.setPositiveButton(R.string.siparis_ver, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Alilanlar").child(kullanici.getId()).push();
                String id = databaseReference.getKey();
                siparis.setYemekId(yemek.getId());
                siparis.setSahipId(yemek.getSahipId());
                siparis.setResimUrl(yemek.getResimUrl());
                siparis.setYemekAdi(yemek.getAdi());
                siparis.setYemekFiyati(yemek.getFiyat());
                siparis.setYemekFiyatBirimi(yemek.getFiyatBirimi());
                siparis.setSiparisMiktari(Integer.parseInt(spinner.getSelectedItem().toString()));
                siparis.setSiparisMesaj(editText.getText().toString());
                siparis.setId(id);
                databaseReference.setValue(siparis);

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Satilanlar").child(yemek.getSahipId()).child(id);
                databaseReference.setValue(siparis);


            }
        });
        alertDialog = dialogBuilder.create();

        alertDialog.show();
    }

    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(AnaSayfaFragment.this);

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                if(currentLat == 0 && currentLon == 0){
                    LatLngBounds TURKIYE = new LatLngBounds(
                            new LatLng(36, 26), new LatLng(42, 45));

                    // Set the camera to the greatest possible zoom level that includes the
                    // bounds
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(TURKIYE, 10));
                }



            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latlng) {
                // TODO Auto-generated method stub
                System.out.println("setOnMapClickListener : "+latlng.toString());
                currentLat = latlng.latitude;
                currentLon = latlng.longitude;
                if(marker !=null){
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(currentLat, currentLon))
                        .title("Bu Nokta Konumunuz Olarak Belirlendi")
                        .snippet(null)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                );

                marker.showInfoWindow();

            }
        });

    }



}
