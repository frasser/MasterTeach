package com.example.frasser.masterteach.dataItems;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Juan Pablo on 7/03/2018.
 */

public class DataItem {
    private Bitmap id;
    private String nobreprofesor;
    private String textDescrip;
    private String precio;
    private String horas;


    public DataItem(){

    }

    public DataItem(Bitmap id, String nobreprofesor, String textDescrip, String precio, String horas) {
        this.id = id;
        this.nobreprofesor = nobreprofesor;
        this.textDescrip = textDescrip;
        this.precio = precio;
        this.horas = horas;

    }


    public Bitmap getId() {
        return id;
    }

    public void setId(Bitmap id) {
        this.id = id;
    }

    public String getNobreprofesor() {
        return nobreprofesor;
    }

    public void setNobreprofesor(String nobreprofesor) {
        this.nobreprofesor = nobreprofesor;
    }

    public String getTextDescrip() {
        return textDescrip;
    }

    public void setTextDescrip(String textDescrip) {
        this.textDescrip = textDescrip;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }
}
