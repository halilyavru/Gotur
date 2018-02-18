package com.getirgotur.Siparisler;

import java.io.Serializable;

/**
 * Created by halilmac on 17/02/2018.
 */

public class Siparis implements Serializable {

    private String id;
    private String tarih;
    private String saat;
    private String yemekId;
    private String resimUrl;
    private String yemekAdi;
    private String yemekFiyati;
    private String yemekFiyatBirimi;
    private String sahipId;
    private String aliciId;
    private String aliciAdi;
    private String aliciKonum;
    private String saticiKonum;
    private int siparisMiktari;
    private boolean teslimatDurumu;
    private boolean siparisKabul;
    private String siparisMesaj;


    public Siparis() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getSaat() {
        return saat;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }

    public String getYemekId() {
        return yemekId;
    }

    public void setYemekId(String yemekId) {
        this.yemekId = yemekId;
    }

    public String getResimUrl() {
        return resimUrl;
    }

    public void setResimUrl(String resimUrl) {
        this.resimUrl = resimUrl;
    }

    public String getYemekAdi() {
        return yemekAdi;
    }

    public void setYemekAdi(String yemekAdi) {
        this.yemekAdi = yemekAdi;
    }

    public String getYemekFiyati() {
        return yemekFiyati;
    }

    public void setYemekFiyati(String yemekFiyati) {
        this.yemekFiyati = yemekFiyati;
    }

    public String getYemekFiyatBirimi() {
        return yemekFiyatBirimi;
    }

    public void setYemekFiyatBirimi(String yemekFiyatBirimi) {
        this.yemekFiyatBirimi = yemekFiyatBirimi;
    }

    public String getSahipId() {
        return sahipId;
    }

    public void setSahipId(String sahipId) {
        this.sahipId = sahipId;
    }

    public String getAliciId() {
        return aliciId;
    }

    public void setAliciId(String aliciId) {
        this.aliciId = aliciId;
    }

    public String getAliciAdi() {
        return aliciAdi;
    }

    public void setAliciAdi(String aliciAdi) {
        this.aliciAdi = aliciAdi;
    }

    public String getAliciKonum() {
        return aliciKonum;
    }

    public void setAliciKonum(String aliciKonum) {
        this.aliciKonum = aliciKonum;
    }

    public int getSiparisMiktari() {
        return siparisMiktari;
    }

    public void setSiparisMiktari(int siparisMiktari) {
        this.siparisMiktari = siparisMiktari;
    }

    public boolean isTeslimatDurumu() {
        return teslimatDurumu;
    }

    public void setTeslimatDurumu(boolean teslimatDurumu) {
        this.teslimatDurumu = teslimatDurumu;
    }

    public boolean isSiparisKabul() {
        return siparisKabul;
    }

    public void setSiparisKabul(boolean siparisKabul) {
        this.siparisKabul = siparisKabul;
    }

    public String getSiparisMesaj() {
        return siparisMesaj;
    }

    public void setSiparisMesaj(String siparisMesaj) {
        this.siparisMesaj = siparisMesaj;
    }

    public String getSaticiKonum() {
        return saticiKonum;
    }

    public void setSaticiKonum(String saticiKonum) {
        this.saticiKonum = saticiKonum;
    }
}
