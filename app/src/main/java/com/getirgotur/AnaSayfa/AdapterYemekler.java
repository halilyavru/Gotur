package com.getirgotur.AnaSayfa;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.getirgotur.R;
import com.getirgotur.Yemek;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by halilmac on 18/02/2018.
 */

public class AdapterYemekler extends RecyclerView.Adapter<AdapterYemekler.YemekViewHolder> {

    private Context mContext;
    private List<Yemek> listYemek;

    public class YemekViewHolder extends RecyclerView.ViewHolder {
        public TextView tvYemekAdi, tvKullaniciAdi, tvPuan;
        public ImageView ivYemek, ivKullanici;

        public YemekViewHolder(View view) {
            super(view);
            tvYemekAdi = (TextView) view.findViewById(R.id.tv_yemek_adi);
            tvKullaniciAdi = (TextView) view.findViewById(R.id.tv_kullanici_adi);
            ivYemek = (ImageView) view.findViewById(R.id.iv_yemek_resim);
            ivKullanici = (ImageView) view.findViewById(R.id.iv_kullanici);
            tvPuan = (TextView) view.findViewById(R.id.tv_puan);
        }
    }


    public AdapterYemekler(Context mContext, List<Yemek> albumList) {
        this.mContext = mContext;
        this.listYemek = albumList;
    }

    @Override
    public YemekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_ana_sayfa_yemekler, parent, false);

        return new YemekViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final YemekViewHolder holder, int position) {
        Yemek yemek = listYemek.get(position);
        holder.tvYemekAdi.setText(yemek.getAdi());
        holder.tvPuan.setText(""+yemek.getPuan());
       holder.tvKullaniciAdi.setText(yemek.getSahipAdi());

        Picasso.with(mContext).load(yemek.getResimUrl())
                .memoryPolicy (MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(holder.ivYemek);

    }





    @Override
    public int getItemCount() {
        return listYemek.size();
    }
}
