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
    private List<String>  listSiparislerim;
    private List<String> listYemeklerim;
    private String konum;
    private int maxYemekGoturmeMesafesi;


    public Kullanici() {
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

    public List<String> getListSiparislerim() {
        return listSiparislerim;
    }

    public void setListSiparislerim(List<String> listSiparislerim) {
        this.listSiparislerim = listSiparislerim;
    }

    public List<String> getListYemeklerim() {
        return listYemeklerim;
    }

    public void setListYemeklerim(List<String> listYemeklerim) {
        this.listYemeklerim = listYemeklerim;
    }

    public String getKonum() {
        return konum;
    }

    public void setKonum(String konum) {
        this.konum = konum;
    }

    public int getMaxYemekGoturmeMesafesi() {
        return maxYemekGoturmeMesafesi;
    }

    public void setMaxYemekGoturmeMesafesi(int maxYemekGoturmeMesafesi) {
        this.maxYemekGoturmeMesafesi = maxYemekGoturmeMesafesi;
    }
}
