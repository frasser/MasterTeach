package com.example.frasser.masterteach.dominio;

import android.net.Uri;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by Juan Pablo on 12/03/2018.
 */

public interface IUsuarioLogic {

    public void crearProfesor(String cedula, String nombres, String apellidos, String login, String clave, Long tipousua, Long tipodocc, Long pago, Uri hojaVida)throws Exception;
    public void crearAlumno(String cedula, String nombres, String apellidos, String login, String clave, Long tipousua, Long tipodocc, Long pago)throws Exception;
}
