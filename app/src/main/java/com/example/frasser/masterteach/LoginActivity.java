package com.example.frasser.masterteach;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.frasser.masterteach.adapters.ItemRowAdapterMenu;
import com.example.frasser.masterteach.dataItems.DataItem;
import com.example.frasser.masterteach.dto.PerfilDTO;
import com.example.frasser.masterteach.dto.UsuarioProfesorDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private FloatingActionButton nuevo;
    private EditText pass;
    private EditText usu;
    FirebaseAuth firebaseAuth;

    static public long tipo ;
    static public long ced;
    static public String log;
    static public String nomUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        /// bandera de validacion de transparencia en status bar para versiones posteriores a kitkat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        tipo =0;

        nuevo = (FloatingActionButton) findViewById(R.id.btn_new);

        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(i);


            }
        });
        pass = findViewById(R.id.edt_password);
        usu = findViewById(R.id.edt_user);
        firebaseAuth = FirebaseAuth.getInstance();


    }

    public void iniciarSesion(View view) {

        if (usu.getText() == null || pass.getText().toString().equals("")) {
            mostrarMensaje("Ingrese usuario y clave");


        } else {


            try {
                firebaseAuth.signInWithEmailAndPassword(usu.getText().toString()
                        , pass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {



                        if (task.isSuccessful()) {











                                Intent intent = new Intent();





                                    intent.setClass(LoginActivity.this, MenuPrincipalActivity.class);

                                    startActivity(intent);
                                    finish();







                        }else {
                            mostrarMensaje("Usuario o Clave invalida");
                        }
                    }
                });


            } catch (Exception e) {
                mostrarMensaje(e.getMessage());
            }

        }
    }


    public void mostrarMensaje(String mensaje) {
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_LONG);
        toast.show();
    }


    public void consultar() {



    try {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("usuarios");
        Query query = databaseReference.orderByChild("login").equalTo(new String(usu.getText().toString()));

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren().iterator().hasNext()) {

                    Log.e("ENCONTRO", dataSnapshot.getChildren().iterator().next().getKey());
                    UsuarioProfesorDTO usuarioDTO = dataSnapshot.getChildren().iterator().next().getValue(UsuarioProfesorDTO.class);

                    tipo = usuarioDTO.getTipo_usu();

                    ced = usuarioDTO.getCedula();
                    log = usuarioDTO.getLogin();
                    nomUsuario = usuarioDTO.getNombres();


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
    }


