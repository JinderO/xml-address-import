package com.jindero.xmlimport.xmladdressimport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "obce")
public class Obec {

    @Id
    @Column(name = "kod")
    private String kod;

    @Column(name = "nazev")
    private String nazev;

    public Obec() {
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
}
