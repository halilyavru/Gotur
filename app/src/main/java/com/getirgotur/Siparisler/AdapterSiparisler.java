package com.getirgotur.Siparisler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.getirgotur.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by halilmac on 18/02/2018.
 */

public class AdapterSiparisler extends RecyclerView.Adapter<AdapterSiparisler.SiparislerViewHolder> {

    private Context mContext;
    private List<Siparis> listSiparis;
    private DatabaseReference databaseReference;
    private boolean satici = false;
    private HashMap<String,Integer> mesafeler;

    public class SiparislerViewHolder extends RecyclerView.ViewHolder {
        public TextView tvYemekAdi, tvStok, tvTutar, tvMesafe, tvSiparisDurumu, tvMesaj;
        public ImageView ivYemek;
        public Button btnOnayla;


        public SiparislerViewHolder(View view) {
            super(view);
            tvYemekAdi = (TextView) view.findViewById(R.id.tv_yemek_adi);
            tvStok = (TextView) view.findViewById(R.id.tv_stok);
            ivYemek = (ImageView) view.findViewById(R.id.iv_yemek_resim);
            tvTutar = (TextView) view.findViewById(R.id.tv_tutar);
            tvMesafe = (TextView) view.findViewById(R.id.tv_mesafe);
            btnOnayla = (Button) view.findViewById(R.id.btn_onayla);
            tvSiparisDurumu =(TextView) view.findViewById(R.id.siparis_durumu);
            tvMesaj =(TextView) view.findViewById(R.id.tv_mesaj);

            if(!satici){
                btnOnayla.setText("Teslimat Onayla");
            }
        }

    }


    public AdapterSiparisler(Context mContext, List<Siparis> listSiparis,boolean satici, HashMap<String,Integer> mesafeler) {

        this.mContext = mContext;
        this.listSiparis = listSiparis;
        this.satici = satici;
        this.mesafeler = mesafeler;

    }

    @Override
    public AdapterSiparisler.SiparislerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_siparisler, parent, false);

        return new AdapterSiparisler.SiparislerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdapterSiparisler.SiparislerViewHolder holder, int position) {
        final Siparis siparis = listSiparis.get(holder.getAdapterPosition());
        holder.tvYemekAdi.setText(siparis.getYemekAdi());

        Double tutar = Double.parseDouble(siparis.getYemekFiyati())*siparis.getSiparisMiktari();
        holder.tvTutar.setText(String.valueOf(tutar));
        holder.tvMesaj.setText(siparis.getSiparisMesaj() != null ? siparis.getSiparisMesaj() : "");

        Picasso.with(mContext).load(siparis.getResimUrl())
                .memoryPolicy (MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(holder.ivYemek);


        if(satici){
            if(mesafeler != null){
                holder.tvMesafe.setText( mesafeler.get(siparis.getAliciId())<=1000 ? (mesafeler.get(siparis.getAliciId())+ "m") : (mesafeler.get(siparis.getAliciId())/1000)+ "KM");

            }

            if(siparis.isSiparisKabul()){
                holder.btnOnayla.setVisibility(View.GONE);
                holder.tvSiparisDurumu.setVisibility(View.VISIBLE);
                if(siparis.isTeslimatDurumu()){
                    holder.tvSiparisDurumu.setText("Teslim Edildi.");
                }else{
                    holder.tvSiparisDurumu.setText("Teslimat Aşamasında.");
                }



            }else{

                holder.btnOnayla.setVisibility(View.VISIBLE);
                holder.tvSiparisDurumu.setVisibility(View.GONE);
                holder.btnOnayla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        siparis.setSiparisKabul(true);
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Satilanlar");
                        databaseReference.child(siparis.getSahipId()).child(siparis.getId()).child("siparisKabul").setValue(true);
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Alilanlar");
                        databaseReference.child(siparis.getAliciId()).child(siparis.getId()).child("siparisKabul").setValue(true);

                    }
                });

            }

        }else{
            if(mesafeler != null) {
                holder.tvMesafe.setText(mesafeler.get(siparis.getSahipId()) <= 1000 ? (mesafeler.get(siparis.getSahipId()) + "m") : (mesafeler.get(siparis.getSahipId()) / 1000) + "KM");

            }
            if(siparis.isSiparisKabul()){

                if(siparis.isTeslimatDurumu()){
                    holder.btnOnayla.setVisibility(View.GONE);
                    holder.tvSiparisDurumu.setVisibility(View.VISIBLE);
                    holder.tvSiparisDurumu.setText("Teslim Edildi.");
                }else{
                    holder.btnOnayla.setVisibility(View.VISIBLE);
                    holder.tvSiparisDurumu.setVisibility(View.GONE);
                    holder.btnOnayla.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Satilanlar");
                            databaseReference.child(siparis.getSahipId()).child(siparis.getId()).child("teslimatDurumu").setValue(true);
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Alilanlar");
                            databaseReference.child(siparis.getAliciId()).child(siparis.getId()).child("teslimatDurumu").setValue(true);

                        }
                    });
                }



            }else{

                holder.tvSiparisDurumu.setVisibility(View.VISIBLE);
                holder.btnOnayla.setVisibility(View.GONE);
                holder.tvSiparisDurumu.setText("Sipariş Onay Bekliyor.");

            }


        }


    }
    @Override
    public int getItemCount() {
        return listSiparis.size();
    }




}
