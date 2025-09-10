package org.example.model;

import java.io.Serializable;

public class Student implements Serializable {
    private int masv;
    private String hoten;
    private int tuoi;
    private String sdt;

    // constructor KHÔNG có Masv (dùng cho ADD)
    public Student(String hoten, int tuoi, String sdt) {
        this.hoten = hoten;
        this.tuoi = tuoi;
        this.sdt = sdt;
    }

    public Student(int masv, String hoten, int tuoi, String sdt) {
        this.masv = masv;
        this.hoten = hoten;
        this.tuoi = tuoi;
        this.sdt = sdt;
    }



    // Getter & Setter
    public int getMasv() { return masv; }
    public String getHoten() { return hoten; }
    public int getTuoi() { return tuoi; }
    public String getSdt() { return sdt; }

    @Override
    public String toString() {
        return masv + " - " + hoten + " - " + tuoi + " - " + sdt;
    }
}
