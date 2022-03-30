package com.example.frasser.masterteach;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frasser.masterteach.dominio.IPerfilLogic;
import com.example.frasser.masterteach.dominio.PerfilLogic;
import com.example.frasser.masterteach.dominio.UsuarioLogic;
import com.example.frasser.masterteach.dto.PerfilDTO;
import com.example.frasser.masterteach.dto.UsuarioProfesorDTO;
import com.example.frasser.masterteach.fragments.EditarPerfilFragments;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Juan Pablo on 9/03/2018.
 */

public class PerfilActivity extends AppCompatActivity {
    static public TextView nombreUsuario;
    static public TextView temaUsuario;
    static public TextView perfilUsuario;
    static public TextView experienciaUsuario;
    static public TextView precioUsuario;
    private ImageButton editar;
    private ImageButton salir;
    static public ImageView foto;

    long cedulaClave = LoginActivity.ced;
    static public Bitmap fotoPro;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        getSupportActionBar().hide();

        nombreUsuario = findViewById(R.id.txt_nombres_perfil);
        temaUsuario = findViewById(R.id.txt_tematica_plano);
        perfilUsuario = findViewById(R.id.texto_plano);
        experienciaUsuario = findViewById(R.id.txt_cantidad_horas);
        precioUsuario = findViewById(R.id.txt_precio_fijado);
        editar = (ImageButton) findViewById(R.id.btn_editar);
        salir = findViewById(R.id.btn_logout_perfil);
        foto = findViewById(R.id.imagn_perfil);

        nombreUsuario.setText(LoginActivity.nomUsuario);




        consultarImagenProfe();
        consultar();
















    }
    public void irEditar(View view){


        android.support.v4.app.Fragment fragment = new EditarPerfilFragments();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_fragment_editar, fragment).addToBackStack("EditarPerfilFragment").commit();



    }

    public void cerrarSesionPerfil(View view){
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        if (usuario!=null){
            FirebaseAuth.getInstance().signOut();
        }
        Intent intentInicio = new Intent();
        intentInicio.setClass(this,LoginActivity.class);
        startActivity(intentInicio);
        finish();
    }
    public void consultar() {



        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("perfil");
            Query query = databaseReference.orderByChild("cedula").equalTo(new Long(LoginActivity.ced));

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildren().iterator().hasNext()) {

                        Log.e("ENCONTRO", dataSnapshot.getChildren().iterator().next().getKey());
                        PerfilDTO perfilDTO = dataSnapshot.getChildren().iterator().next().getValue(PerfilDTO.class);

                        perfilUsuario.setText(perfilDTO.getDescripcion());
                        experienciaUsuario.setText(perfilDTO.getExperiencia());
                        precioUsuario.setText(perfilDTO.getPrecio());
                        temaUsuario.setText(perfilDTO.getTema());


                        if (perfilUsuario.getText().toString().length() == 0 ){
                            android.support.v4.app.Fragment fragment = new EditarPerfilFragments();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.container_fragment_editar, fragment).addToBackStack("EditarPerfilFragment").commit();




                        }





                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            Log.i("ERROR","PROBLEMAS CON "+e.getMessage());
        }




    }

    public void consultarImagenProfe() {

        String c = String.valueOf(cedulaClave);

        if (!c.equals("")) {
            long tamano = 1024 * 1024;

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://master-teach.appspot.com");
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference downloadStorageReference = storageReference.child("fotos/" + c.toString() + ".png");

            downloadStorageReference.getBytes(tamano).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Log.e("ERROR", "entry point");
                    fotoPro = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    //
                    Bitmap original = ((Bitmap) fotoPro);

                    RoundedBitmapDrawable roundedDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), original);
                    roundedDrawable.setCornerRadius(original.getHeight());

                    //foto.setImageBitmap(fotoPro);
                    //
                    foto.setImageDrawable(roundedDrawable);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("ERROR", "FALLO DESCARGAR IMAGEN" + e.getMessage());
                }
            });
        }
    }



}
