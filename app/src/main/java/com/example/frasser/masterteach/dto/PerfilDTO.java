package com.example.frasser.masterteach.dto;

import android.net.Uri;

/**
 * Created by Juan Pablo on 13/03/2018.
 */

public  class PerfilDTO {

    private Long cedula;
    private String nombre;
    private String descripcion;

    private String experiencia;
    private String precio;
    private String tema;

    public PerfilDTO() {

    }

   // public  void PerfilDTO(){

    //}

    public PerfilDTO(Long cedula,String nombre,String descripcion,String experiencia, String precio, String tema) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.descripcion = descripcion;

        this.experiencia = experiencia;
        this.precio = precio;
        this.tema = tema;

    }



    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getCedula() {
        return cedula;
    }

    public void setCedula(Long cedula) {
        this.cedula = cedula;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
