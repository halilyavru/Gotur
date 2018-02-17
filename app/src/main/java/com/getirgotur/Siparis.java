package com.getirgotur;

import java.io.Serializable;

/**
 * Created by halilmac on 17/02/2018.
 */

public class Siparis implements Serializable {

    private String id;
    private String tarih;
    private String saat;
    private String saticiId;
    private String aliciId;
    private String urunId;
    private String urunFiyati;
    private int teslimatDurumu;

    public Siparis() {
    }

    public Siparis(String id, String tarih, String saat, String saticiId, String aliciId, String urunId, String urunFiyati, int teslimatDurumu) {
        this.id = id;
        this.tarih = tarih;
        this.saat = saat;
        this.saticiId = saticiId;
        this.aliciId = aliciId;
        this.urunId = urunId;
        this.urunFiyati = urunFiyati;
        this.teslimatDurumu = teslimatDurumu;
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

    public String getSaticiId() {
        return saticiId;
    }

    public void setSaticiId(String saticiId) {
        this.saticiId = saticiId;
    }

    public String getAliciId() {
        return aliciId;
    }

    public void setAliciId(String aliciId) {
        this.aliciId = aliciId;
    }

    public String getUrunId() {
        return urunId;
    }

    public void setUrunId(String urunId) {
        this.urunId = urunId;
    }

    public String getUrunFiyati() {
        return urunFiyati;
    }

    public void setUrunFiyati(String urunFiyati) {
        this.urunFiyati = urunFiyati;
    }

    public int getTeslimatDurumu() {
        return teslimatDurumu;
    }

    public void setTeslimatDurumu(int teslimatDurumu) {
        this.teslimatDurumu = teslimatDurumu;
    }
}
