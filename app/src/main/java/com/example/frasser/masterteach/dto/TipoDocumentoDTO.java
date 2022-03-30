package com.example.frasser.masterteach.dto;

/**
 * Created by Juan Pablo on 9/03/2018.
 */

public class TipoDocumentoDTO {
    private Long codigo;
    private String nombre;

    private TipoDocumentoDTO(){

    }

    public TipoDocumentoDTO(Long codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String toString(){
        return codigo+".   "+nombre;
    }
}
