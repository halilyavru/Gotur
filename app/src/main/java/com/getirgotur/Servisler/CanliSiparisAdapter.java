package com.getirgotur.Servisler;

/**
 * Created by halilmac on 18/02/2018.
 */

import android.app.Service;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.getirgotur.R;
import com.getirgotur.Siparis;

import java.util.ArrayList;
import java.util.List;

public class CanliSiparisAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Siparis> siparisler= new ArrayList<>();
    private RecyclerView mRecylerView;
    private int kacinciUrun=0;
    private String altKategori="";
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    public class SiparisAktif extends RecyclerView.ViewHolder {
        public TextView urunAdi,urunAciklama,urunFiyati;
        public ImageView urunResim,direkEkle;
        private ImageView rlDirektEkle;

        public SiparisAktif(View view) {
            super(view);
            /*
            urunAdi = (TextView) view.findViewById(R.id.urun_adi);
            urunAciklama = (TextView) view.findViewById(R.id.urun_aciklama);
            urunFiyati = (TextView) view.findViewById(R.id.urun_fiyati);
            urunResim = (ImageView) view.findViewById(R.id.urun_resim);
            direkEkle = (ImageView) view.findViewById(R.id.sepete_direkt_ekle);
*/

        }
    }

    public class SiparisPasif extends RecyclerView.ViewHolder{
        public TextView urunKategoriTitle;
        public SiparisPasif(View itemView) {
            super(itemView);
            //urunKategoriTitle = (TextView) itemView.findViewById(R.id.sipa);
        }
    }


    public CanliSiparisAdapter(RecyclerView mrecyler, Service mContext) {

        SiparisCanliTakipServis serviceSiparisler= (SiparisCanliTakipServis) mContext;


        /*
        this.mContext = mContext;
        MainActivity a= (MainActivity) mContext;
        for(int i=0;i<a.restoran.getUrunler().size();i++)
        {
            this.urunler.addAll(a.restoran.getUrunler().get(i));
        }


        String altKAtegori="";
        int extra=0;
        for(int i=0;i<urunler.size();i++)
        {
            if (!urunler.get(i).getUrun_kategori().equalsIgnoreCase(altKAtegori)&&!urunler.get(i).getUrun_kategori().equalsIgnoreCase(""))
            {
                extra++;
                altKAtegori=urunler.get(i).getUrun_kategori();
            }
        }
        int sayac=0;
        for(int i=0;i<(urunler.size()+extra);i++)
        {

            if(urunler.get(sayac).getUrun_kategori().equalsIgnoreCase(altKategori))
            {
                ara_barlar.put(i,urunler.get(sayac));
                sayac++;
            }
            else
            {
                ara_barlar.put(i,null);
                altKategori=urunler.get(sayac).getUrun_kategori();
            }
            System.out.println("ara"+ara_barlar.get(i));
        }

        this.mRecylerView=mrecyler;
        this.activity=mContext;
        */

    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;


        if (TYPE_HEADER == viewType) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_siparis_aktif, parent, false);
            return new SiparisAktif(itemView);
        }else{
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_siparis_pasif, parent, false);

/*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = mRecylerView.getChildLayoutPosition(view);
                    if(ara_barlar.get(itemPosition)!=null)
                    {
                        MainActivity mainActivity = (MainActivity)activity;
                        mainActivity.navItemIndex = 6;

                        mainActivity.seciliUrun=ara_barlar.get(itemPosition);

                        mainActivity.loadHomeFragment();
                    }



                }
            });
*/
            return new SiparisPasif(itemView);
        }



    }

    @Override
    public int getItemViewType(int position) {
        return 0;

        //return 1;

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof SiparisPasif){
            SiparisPasif mTitleViewHolder = (SiparisPasif)holder;
            //mTitleViewHolder.urunKategoriTitle.setText(mesajlar.get(position).getMessage()+"");
        }else if(holder instanceof SiparisAktif){

        }




    }

    @Override
    public int getItemCount() {

        return 3;
    }

}
