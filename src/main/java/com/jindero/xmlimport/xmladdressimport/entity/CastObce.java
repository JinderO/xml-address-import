package com.jindero.xmlimport.xmladdressimport.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "casti_obce")
public class CastObce {

    @Id
    @Column(name = "kod")
    private String kod;

    @Column(name = "nazev")
    private String nazev;

    @Column(name = "kod_obce")
    private String kodObce;

    public CastObce() {
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getKodObce() {
        return kodObce;
    }

    public void setKodObce(String kodObce) {
        this.kodObce = kodObce;
    }
}
