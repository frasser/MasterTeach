package com.example.frasser.masterteach.accesoDato;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.frasser.masterteach.dto.PerfilDTO;

/**
 * Created by Juan Pablo on 13/03/2018.
 */

public interface IPerfilDAO {
    public  void crearPerfil(PerfilDTO perfilDTO)throws Exception;
    public void almacenarFoto (Long cedulaProfe, Bitmap foto);
}
