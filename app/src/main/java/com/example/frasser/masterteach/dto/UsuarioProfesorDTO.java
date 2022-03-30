package com.example.frasser.masterteach.dto;

/**
 * Created by Juan Pablo on 12/03/2018.
 */

public class UsuarioProfesorDTO {

    private Long cedula;
    private String nombres;
    private String apellidos;
    private String login;
    private String clave;
    private Long tipo_usu;
    private Long tipo_doc;
    private Long pago;


    private UsuarioProfesorDTO(){

    }

    public UsuarioProfesorDTO(Long cedula, String nombres, String apellidos, String login, String clave, Long tipo_usu, Long tipo_doc,Long pago) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.login = login;
        this.clave = clave;
        this.tipo_usu = tipo_usu;
        this.tipo_doc = tipo_doc;
        this.pago = pago;

    }

    public Long getPago() {
        return pago;
    }

    public void setPago(Long pago) {
        this.pago = pago;
    }

    public Long getCedula() {
        return cedula;
    }

    public void setCedula(Long cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Long getTipo_usu() {
        return tipo_usu;
    }

    public void setTipo_usu(Long tipo_usu) {
        this.tipo_usu = tipo_usu;
    }

    public Long getTipo_doc() {
        return tipo_doc;
    }

    public void setTipo_doc(Long tipo_doc) {
        this.tipo_doc = tipo_doc;
    }




}
