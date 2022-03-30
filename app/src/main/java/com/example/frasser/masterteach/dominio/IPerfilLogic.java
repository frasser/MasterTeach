package com.example.frasser.masterteach.dominio;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Juan Pablo on 13/03/2018.
 */

public interface IPerfilLogic {
    public void crearPerfil(Long cedula, String nombre, String descripcion, String experiencia, String precio, String tema, Bitmap imagenProfesor)throws Exception;
}
