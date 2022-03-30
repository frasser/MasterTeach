package com.example.frasser.masterteach.dominio;

import android.net.Uri;

import com.example.frasser.masterteach.accesoDato.IUsuarioDAO;
import com.example.frasser.masterteach.accesoDato.UsuarioDAO;
import com.example.frasser.masterteach.dto.UsuarioProfesorDTO;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by Juan Pablo on 12/03/2018.
 */

public class UsuarioLogic implements IUsuarioLogic {
    @Override
    public void crearProfesor(String cedula,String nombres,String apellidos,String login,String clave,Long tipousua,Long tipodocc,Long pago, Uri hojaVida) throws Exception {

        try {
            if (cedula.length() != 10 && cedula.length() != 11){
                throw new Exception("el numero de documento debe tener entre 10 y 11 digitos");
            }
            if (clave == null || clave.equals("")){
                throw new Exception("la clave de usuario es obligatoria");

            }
            if (clave.length() <6){
                throw new Exception("la clave debe tener minimo 6 caracteres");
            }
            if (login == null || login.equals("")){
                throw new Exception("campo login es obligatorio");
            }
            if (nombres == null || nombres.equals("")){
                throw new Exception("campo nombres es obligatorio");
            }
            if (apellidos == null || apellidos.equals("")){
                throw new Exception("campo apellidos es obligatorio");

            }
            if (tipousua == null){
                throw new Exception("seleccione tipo usuario");
            }
            if (tipodocc == null){
                throw new Exception("seleccione tipo de documento");
            }
            //if(hojaVida == null){
              //  throw  new Exception("debe cargar su curriculum vitae, presione examinar y ubique el documento en su dispositivo");
            //}


            IUsuarioDAO iUsuarioDAO = new UsuarioDAO();
            iUsuarioDAO.almacenarHv(new Long(cedula),hojaVida);
            iUsuarioDAO.crearProfesor(new UsuarioProfesorDTO(new Long(cedula),nombres,apellidos,login,clave,tipousua,tipodocc,pago));


        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public void crearAlumno(String cedula, String nombres, String apellidos, String login, String clave, Long tipousua, Long tipodocc, Long pago) throws Exception {

        try {
            if (cedula.length() != 10 && cedula.length() != 11){
                throw new Exception("el numero de documento debe tener entre 10 y 11 digitos");
            }
            if (clave == null || clave.equals("")){
                throw new Exception("la clave de usuario es obligatoria");

            }
            if (clave.length() <6){
                throw new Exception("la clave debe tener minimo 6 caracteres");
            }
            if (login == null || login.equals("")){
                throw new Exception("campo login es obligatorio");
            }
            if (nombres == null || nombres.equals("")){
                throw new Exception("campo nombres es obligatorio");
            }
            if (apellidos == null || apellidos.equals("")){
                throw new Exception("campo apellidos es obligatorio");

            }
            if (tipousua == null){
                throw new Exception("seleccione tipo usuario");
            }
            if (tipodocc == null){
                throw new Exception("seleccione tipo de documento");
            }
            //if(hojaVida == null){
            //  throw  new Exception("debe cargar su curriculum vitae, presione examinar y ubique el documento en su dispositivo");
            //}


            IUsuarioDAO iUsuarioDAO = new UsuarioDAO();

            iUsuarioDAO.crearProfesor(new UsuarioProfesorDTO(new Long(cedula),nombres,apellidos,login,clave,tipousua,tipodocc,pago));


        }catch (Exception e){
            throw e;
        }

    }
}
