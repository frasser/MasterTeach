package com.example.frasser.masterteach;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.frasser.masterteach.adapters.ItemRowAdapterMenu;
import com.example.frasser.masterteach.dataItems.DataItem;
import com.example.frasser.masterteach.dto.PerfilDTO;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan Pablo on 7/03/2018.
 */

public class MenuPrincipalActivity extends AppCompatActivity {

    private EditText campoBusqueda;
    private ImageButton search;
    private FirebaseAuth mAuth;
    private ImageView cerrar;

    private List<DataItem> mUploads;
    private StorageReference mDatabaseRef;


    List<DataItem> dataItemList;
    Bitmap fotoProfe;

    String ced;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        getSupportActionBar().hide();

        /// bandera de validacion de transparencia en status bar para versiones posteriores a kitkat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        consultarMaestros();



        //campoBusqueda = findViewById(R.id.edt_campo_busqueda);
        search = (ImageButton) findViewById(R.id.btn_search);
        mAuth = FirebaseAuth.getInstance();
        cerrar = findViewById(R.id.btn_logout);

        mUploads = new ArrayList<>();



    }

    public void consultarMaestros() {

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("perfil");

        dataItemList = new ArrayList<DataItem>();

        ////////////////////////////////////////////////////

        //ced = perfil.getCedula().toString();

        if (ced != null) {
            long tamano = 1024 * 1024;

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://proyectoclase-91dbd.appspot.com");
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference downloadStorageReference = storageReference.child("fotos/55555555555.png");

            downloadStorageReference.getBytes(tamano).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Log.e("ERROR", "entry point");
                    fotoProfe = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    //
                    //Bitmap original = ((Bitmap) fotoEstudiante);

                    // RoundedBitmapDrawable roundedDrawable =
                    //   RoundedBitmapDrawableFactory.create(getResources(), original);
                    // roundedDrawable.setCornerRadius(original.getHeight());

                    //
                    //  fotoE.setImageBitmap(fotoEstudiante);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("ERROR", "FALLO DESCARGAR IMAGEN" + e.getMessage());
                }
            });
        }


        ///////////////////////////////////////////////////////


        ///////////////////////////////////////////////


        ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getChildren().iterator().hasNext()) {


                    try {

                        Log.e("ENCONTRO", dataSnapshot.getChildren().iterator().next().getKey());


                        for (DataSnapshot iterado : dataSnapshot.getChildren()) {
                            PerfilDTO perfil = iterado.getValue(PerfilDTO.class);//dataSnapshot.getChildren().iterator().next().getValue(PerfilDTO.class);


                            dataItemList.add(new DataItem(fotoProfe, perfil.getNombre(),
                                    perfil.getDescripcion(),
                                    perfil.getPrecio(), perfil.getExperiencia()));
                        }



                        // final ListView listView = findViewById(R.id.listViewCustome);
                        ListView listView = findViewById(R.id.listViewCustome);

                        ItemRowAdapterMenu adapter = new ItemRowAdapterMenu(MenuPrincipalActivity.this, R.layout.adapter_itemrow_menu, dataItemList);
                        listView.setAdapter(adapter);
                    } catch (Exception e) {
                        Log.e("ERROR", "ERROR AL CREAR EL LIST VIEW " + e.getMessage());
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void cerrarSesion(View view) {


        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        if (usuario != null) {
            FirebaseAuth.getInstance().signOut();
        }
        Intent intentInicio = new Intent();
        intentInicio.setClass(this, LoginActivity.class);
        startActivity(intentInicio);
        finish();

    }

    public void pedirClase(View view) {
        Intent intent = new Intent();
        intent.setClass(this, AgendarActivity.class);
        startActivity(intent);

    }

    public void traerFoto() {

        long tamano = 1024 * 1024;
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://proyectoclase-91dbd.appspot.com");
        StorageReference storageReference = firebaseStorage.getReference("fotos");






    }
}


