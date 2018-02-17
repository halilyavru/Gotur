package com.getirgotur;

import java.io.Serializable;
import java.util.List;

/**
 * Created by halilmac on 17/02/2018.
 */

public class Kullanici implements Serializable {


    private String id;
    private String adi;
    private String resimUrl;
    private List<Siparis>  listSiparislerim;
    private List<Yemek> listYemeklerim;
    private int maxYemekGoturmeMesafesi;


    public Kullanici() {
    }

    public Kullanici(String id, String adi, String resimUrl, List<Siparis> listSiparislerim, List<Yemek> listYemeklerim, int maxYemekGoturmeMesafesi) {
        this.id = id;
        this.adi = adi;
        this.resimUrl = resimUrl;
        this.listSiparislerim = listSiparislerim;
        this.listYemeklerim = listYemeklerim;
        this.maxYemekGoturmeMesafesi = maxYemekGoturmeMesafesi;
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

    public List<Siparis> getListSiparislerim() {
        return listSiparislerim;
    }

    public void setListSiparislerim(List<Siparis> listSiparislerim) {
        this.listSiparislerim = listSiparislerim;
    }

    public List<Yemek> getListYemeklerim() {
        return listYemeklerim;
    }

    public void setListYemeklerim(List<Yemek> listYemeklerim) {
        this.listYemeklerim = listYemeklerim;
    }

    public int getMaxYemekGoturmeMesafesi() {
        return maxYemekGoturmeMesafesi;
    }

    public void setMaxYemekGoturmeMesafesi(int maxYemekGoturmeMesafesi) {
        this.maxYemekGoturmeMesafesi = maxYemekGoturmeMesafesi;
    }
}
