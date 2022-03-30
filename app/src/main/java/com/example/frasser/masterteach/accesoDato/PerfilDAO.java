package com.example.frasser.masterteach.accesoDato;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.frasser.masterteach.dto.PerfilDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by Juan Pablo on 13/03/2018.
 */

public class PerfilDAO implements IPerfilDAO {
    @Override
    public void crearPerfil(PerfilDTO perfilDTO) throws Exception {

        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference("perfil");
            reference.child(perfilDTO.getCedula()+"").setValue(perfilDTO);//muestra los valores contenido dentro del nodo usuarioDTO

        }catch (Exception e){
            throw new Exception("Error creando perfil "+e.getMessage());
        }

    }

    @Override
    public void almacenarFoto(Long cedulaProfe, Bitmap foto) {

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
    }
}
