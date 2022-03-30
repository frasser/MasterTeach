package com.example.frasser.masterteach.accesoDato;

import android.net.Uri;

import com.example.frasser.masterteach.dto.UsuarioProfesorDTO;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by Juan Pablo on 12/03/2018.
 */

public interface IUsuarioDAO {
    public void crearProfesor(UsuarioProfesorDTO usuarioProfesorDTO)throws Exception;
    public void almacenarHv(Long cedula, Uri hojaVida) throws Exception;

}
