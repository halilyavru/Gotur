package com.getirgotur.Giris;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.getirgotur.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by halilmac on 17/02/2018.
 */

public class GirisFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {

    private LocationManager mLocationManager;
    private GoogleMap mMap;
    private LatLng[] arrayLatLang = {new LatLng(42, 26), new LatLng(36, 26), new LatLng(36, 45), new LatLng(42, 45)};
    private double currentLat = 0;
    private double currentLon = 0;
    private boolean mLocationPermissionGranted = false;
    private static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1453;
    private Marker marker;

    private EditText etKullaniciAdi;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_giris, container, false);

        etKullaniciAdi = (EditText) view.findViewById(R.id.et_kullanici_adi);

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
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, GirisFragment.this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, GirisFragment.this);


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        view.findViewById(R.id.btn_giris_yap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etKullaniciAdi.getText() != null && etKullaniciAdi.getText().toString().trim().length()>3 && currentLat != 0 && currentLon != 0 ){
                    ((GirisActivity)getActivity()).giris(etKullaniciAdi.getText().toString().trim(),(currentLat+","+currentLon));
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.uyari);
                    if(etKullaniciAdi.getText() == null || etKullaniciAdi.getText().toString().trim().length()<3){
                        builder.setMessage("Kullanıcı Adı En az 3 Harften oluşmalı.");
                    }else if(currentLat == 0 || currentLon == 0){
                        builder.setMessage("Harita üzerinden bir konum seçiniz.");
                    }
                    builder.setPositiveButton(R.string.tamam, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();;

                }

            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerClickListener(GirisFragment.this);

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
        /*List<LatLng> listLatLong =  new ArrayList<>();
        listLatLong.add(new LatLng(40.95650372593243,29.109800532460216));
        listLatLong.add(new LatLng(40.9522654778032,29.109902791678905));

        drawRouteOnMap(mMap, listLatLong);*/

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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if(requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }

        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                 getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    @Override
    public void onLocationChanged(Location location) {



        if(currentLat == 0 && currentLon == 0){

            currentLat = location.getLatitude();
            currentLon = location.getLongitude();

            if(marker != null){
                marker.remove();
            }
            marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(currentLat, currentLon))
                    .title("Burayı Konumum Olarak Belirle.")
                    .snippet(null)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            );
            marker.showInfoWindow();
        }
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

        if (marker.equals(marker))
        {

        }
        return false;
    }




}
