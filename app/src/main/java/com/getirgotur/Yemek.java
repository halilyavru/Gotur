package com.getirgotur;

import java.io.Serializable;

/**
 * Created by halilmac on 17/02/2018.
 */

public class Yemek implements Serializable {

    private String id;
    private String adi;
    private String resimUrl;
    private double fiyat;
    private String fiyatBirimi;
    private String malzeme;
    private int stok;
    private String sahipId;
    private double puan;
    private int puanlamaSayisi;

    public Yemek() {
    }

    public Yemek(String id, String adi, String resimUrl, double fiyat, String fiyatBirimi, String malzeme, int stok, String sahipId, double puan, int puanlamaSayisi) {
        this.id = id;
        this.adi = adi;
        this.resimUrl = resimUrl;
        this.fiyat = fiyat;
        this.fiyatBirimi = fiyatBirimi;
        this.malzeme = malzeme;
        this.stok = stok;
        this.sahipId = sahipId;
        this.puan = puan;
        this.puanlamaSayisi = puanlamaSayisi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getResimUrl() {
        return resimUrl;
    }

    public void setResimUrl(String resimUrl) {
        this.resimUrl = resimUrl;
    }

    public double getFiyat() {
        return fiyat;
    }

    public void setFiyat(double fiyat) {
        this.fiyat = fiyat;
    }

    public String getFiyatBirimi() {
        return fiyatBirimi;
    }

    public void setFiyatBirimi(String fiyatBirimi) {
        this.fiyatBirimi = fiyatBirimi;
    }

    public String getMalzeme() {
        return malzeme;
    }

    public void setMalzeme(String malzeme) {
        this.malzeme = malzeme;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getSahipId() {
        return sahipId;
    }

    public void setSahipId(String sahipId) {
        this.sahipId = sahipId;
    }

    public double getPuan() {
        return puan;
    }

    public void setPuan(double puan) {
        this.puan = puan;
    }

    public int getPuanlamaSayisi() {
        return puanlamaSayisi;
    }

    public void setPuanlamaSayisi(int puanlamaSayisi) {
        this.puanlamaSayisi = puanlamaSayisi;
    }
}
