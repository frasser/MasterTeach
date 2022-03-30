package com.example.frasser.masterteach.dominio;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.frasser.masterteach.accesoDato.IPerfilDAO;
import com.example.frasser.masterteach.accesoDato.PerfilDAO;
import com.example.frasser.masterteach.dto.PerfilDTO;

/**
 * Created by Juan Pablo on 13/03/2018.
 */

public class PerfilLogic implements IPerfilLogic {
    @Override
    public void crearPerfil(Long cedula,String nombre, String descripcion,String experiencia, String precio,String tema, Bitmap imagenProfesor) throws Exception {

        try {

            if (descripcion == null || descripcion.equals("")){
                throw new Exception("Debe ingresar una descripcion de su perfil profesional");

            }
            if (experiencia == null || experiencia.equals("")){
                throw new Exception("Ingrese cantidad de horas de experiencia que ostenta");

            }
            if (precio == null || precio.equals("")){
                throw new Exception("Ingrese precio a cobrar por hora ");

            }
            if (imagenProfesor == null){
                throw new Exception("la fotografia es obligatoria");
            }



            IPerfilDAO iPerfilDAO = new PerfilDAO();
            iPerfilDAO.almacenarFoto(new Long(cedula),imagenProfesor);
            iPerfilDAO.crearPerfil(new PerfilDTO(new Long(cedula),nombre,descripcion,experiencia,precio,tema));


        }catch (Exception e){
            throw e;
        }

    }


}
