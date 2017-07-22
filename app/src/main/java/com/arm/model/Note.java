package com.arm.model;

import java.io.Serializable;

/**
 * Created by ARM on 21-Jul-17.
 */

public class Note implements Serializable {
    private String tieuDe;
    private String noiDung;
    private Integer ngay;
    private Integer thang;
    private Integer nam;
    private String key;

    public Note() {
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Integer getNgay() {
        return ngay;
    }

    public void setNgay(Integer ngay) {
        this.ngay = ngay;
    }

    public Integer getThang() {
        return thang;
    }

    public void setThang(Integer thang) {
        this.thang = thang;
    }

    public Integer getNam() {
        return nam;
    }

    public void setNam(Integer nam) {
        this.nam = nam;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
