package com.example.frasser.masterteach.accesoDato;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.frasser.masterteach.dto.UsuarioProfesorDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Created by Juan Pablo on 12/03/2018.
 */

public class UsuarioDAO implements IUsuarioDAO {
    @Override
    public void crearProfesor(UsuarioProfesorDTO usuarioProfesorDTO) throws Exception {
        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("usuarios");
            databaseReference.child(usuarioProfesorDTO.getCedula()+"").setValue(usuarioProfesorDTO);//muestra los valores contenido dentro del nodo usuarioDTO

        }catch (Exception e){
            throw new Exception("Error creando usuario Profesor "+e.getMessage());
        }
    }

    @Override
    public void almacenarHv(Long cedula, Uri hojaVida) throws Exception {

        try {
            FirebaseStorage storage = FirebaseStorage.getInstance("gs://master-teach.appspot.com");
            StorageReference reference = storage.getReference().
                    child("hv/"+cedula+".pdf");

           reference.putFile(hojaVida).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   Log.i("INFO","SE ALMACENO CON EXITO");
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Log.i("INFO","ERROR AL SUBIR LA HOJA DE VIDA");
               }
           });

        }catch (Exception e){
            throw e;
        }

    }
}

/*
  try {
            FirebaseStorage storage = FirebaseStorage.getInstance("gs://master-teach.appspot.com");
            StorageReference storageReference = storage.getReference();
            StorageReference referenciaGuardar = storageReference.child("fotos/"+cedulaProfe+".png");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            foto.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream);
            byte[] bitemapdata = byteArrayOutputStream.toByteArray();

            ByteArrayInputStream bs = new ByteArrayInputStream(bitemapdata);

            UploadTask uploadTask = referenciaGuardar.putStream(bs);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("ERROR","FALLO AL SUBIR FOTO "+e.getMessage());

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("INFO","SE ALMACENO CORRECTAMENTE");
                }
            });

        }catch (Exception e){
            throw e;
        }
 */