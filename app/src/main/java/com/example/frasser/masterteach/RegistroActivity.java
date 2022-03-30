package com.example.frasser.masterteach;

import android.app.usage.ExternalStorageStats;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.RelativeDateTimeFormatter;
import android.media.MediaDataSource;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frasser.masterteach.dominio.IUsuarioLogic;
import com.example.frasser.masterteach.dominio.UsuarioLogic;
import com.example.frasser.masterteach.dto.TemaPrincipalDTO;
import com.example.frasser.masterteach.dto.TipoDocumentoDTO;
import com.example.frasser.masterteach.fragments.EditarPerfilFragments;
import com.example.frasser.masterteach.fragments.RegistroFragments;
import com.example.frasser.masterteach.utilidades.MensajeDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan Pablo on 27/02/2018.
 */

public class RegistroActivity extends AppCompatActivity {

    private Switch swtch;
    private EditText documento;
    private EditText nombres;
    private EditText apellidos;
    private EditText contra;
    private EditText confir;
    private EditText correo;
    private Spinner tipoDocu;

    private Button registrar;
    private Button hv;
    private TextView ruta;

    static public String nombreCreado;
    static public String cedulaCreada;


    private List<TipoDocumentoDTO> listTipoDocu;
    private List<TemaPrincipalDTO> listTema;
    FirebaseAuth firebaseAuth;


    static public int tipousu = 1;// variable tipo de documento definida por el estado del switch

    private final int PICKER = 1;

    File curriculum;
    Uri uri;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        getSupportActionBar().hide();


        /// bandera de validacion de transparencia en status bar para versiones posteriores a kitkat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        swtch = (Switch) findViewById(R.id.switch_tipo);
        swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked){

                    tipousu = 2;
                    limpiar();
                    android.support.v4.app.Fragment fragment = new RegistroFragments();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).addToBackStack("RegistroFragments").commit();

                    hv.setEnabled(false);





                }else {


                     onBackPressed();
                     limpiar();
                     tipousu = 1;

                     hv.setEnabled(true);
                     RegistroFragments.pago.setSelection(0);


                }
            }
        });

        documento = findViewById(R.id.edt_numero_docu);
        nombres = findViewById(R.id.edt_nombre);
        apellidos = findViewById(R.id.edt_apellidos);
        contra = findViewById(R.id.edt_contra_registro);
        confir = findViewById(R.id.edt_confirmar_contra);
        correo = findViewById(R.id.edt_mail);
        tipoDocu = (Spinner) findViewById(R.id.spn_tipo_docu);

        registrar =  findViewById(R.id.btn_registrar);
        hv =  findViewById(R.id.btn_subir_hv);
        ruta = findViewById(R.id.ruta);
        firebaseAuth = FirebaseAuth.getInstance();




        cargarTipoDocumento();



/*
       registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
*/

    }



    public void cargarSpinnerDocumento() {
        ArrayAdapter<TipoDocumentoDTO> adapterDocumento = new ArrayAdapter<TipoDocumentoDTO>(this,android.R.layout.simple_spinner_item,listTipoDocu);
        adapterDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoDocu.setAdapter(adapterDocumento);

        tipoDocu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Error",tipoDocu.getSelectedItem().toString());
                tipoDocu.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void cargarTipoDocumento() {

        try {

            listTipoDocu = new ArrayList<TipoDocumentoDTO>();

            //CONSULTANDO TODOS LOS TIPOS DE DOCUMENTOS
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference("tipodocumento");

            ValueEventListener valueEventListener = reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.i("Info","FIRE BASE DETECTO CAMBIOS");
                    listTipoDocu.clear();

                    for (DataSnapshot iterado : dataSnapshot.getChildren()){
                        TipoDocumentoDTO tipoDocumento = iterado.getValue(TipoDocumentoDTO.class);

                        listTipoDocu.add(tipoDocumento);
                    }

                    cargarSpinnerDocumento();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Log.e("ERROR","ERROR-------> ",e);
        }
    }

    public  void crearUsuario(View view){

        long p= tipoDocu.getSelectedItemId() + 1;


        Long doc= Long.valueOf(p);
        Long us= Long.valueOf(tipousu);

        Long pa=Long.valueOf(RegistroFragments.tipopa);









            if (tipousu == 1) {


                try {


                    IUsuarioLogic iUsuarioLogic = new UsuarioLogic();

                    iUsuarioLogic.crearProfesor(documento.getText().toString(),
                            nombres.getText().toString(),
                            apellidos.getText().toString(),
                            correo.getText().toString(),
                            contra.getText().toString(),
                            us, doc, null,uri);


                    FirebaseUser usuario = firebaseAuth.getCurrentUser(); /// linea encargada de verificar las sesiones de usuario, validando si se abrio en otro lugar o no!

                    if (usuario!=null){

                        Log.i("Info","El correo del usuario es "+usuario.getEmail());
                        Intent intent = new Intent();
                        intent.setClass(this,MenuPrincipalActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    firebaseAuth.createUserWithEmailAndPassword(correo.getText().toString(),contra.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){
                                        nombreCreado = nombres.getText().toString();
                                        cedulaCreada = documento.getText().toString();
                                        limpiar();
                                        new MensajeDialog("Se ha creado el usuario con exito",RegistroActivity.this, PerfilActivity.class)
                                                .show(getFragmentManager(),"Alert");

                                    }else {
                                        mostrarMensaje("No se ha podido registrar, verifique el usuario y clave");
                                    }
                                }
                            });







                } catch (Exception e) {
                    mostrarMensaje(e.getMessage());
                }
            }


            if (tipousu == 2){
                try {



                IUsuarioLogic iUsuarioLogic = new UsuarioLogic();

                iUsuarioLogic.crearAlumno(documento.getText().toString(),
                        nombres.getText().toString(),
                        apellidos.getText().toString(),
                        correo.getText().toString(),
                        contra.getText().toString(),
                        us,doc,pa);

                    FirebaseUser usuario = firebaseAuth.getCurrentUser(); /// linea encargada de verificar las sesiones de usuario, validando si se abrio en otro lugar o no!

                    if (usuario!=null){

                        Log.i("Info","El correo del usuario es "+usuario.getEmail());
                        Intent intent = new Intent();
                        intent.setClass(this,MenuPrincipalActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    firebaseAuth.createUserWithEmailAndPassword(correo.getText().toString(),contra.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){
                                        nombreCreado = nombres.getText().toString();
                                        cedulaCreada = documento.getText().toString();
                                        limpiar();
                                        new MensajeDialog("Se ha creado el usuario con exito",RegistroActivity.this,MenuPrincipalActivity.class)
                                                .show(getFragmentManager(),"Alert");

                                    }else {
                                        mostrarMensaje("No se ha podido registrar, verifique el usuario y clave");
                                    }
                                }
                            });





                }catch (Exception e){
                    mostrarMensaje(e.getMessage());
                }

            }







    }

    public void pickFile (View view){


            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);




        try {
            startActivityForResult(
                    Intent.createChooser(intent,"Seleccione un archivo para subir"),
                    PICKER
            );
        }catch (android.content.ActivityNotFoundException ex){

            Log.e("ERROR","ERROR ABRIENDO GALERIA DE ARCHIVOS "+ex.getMessage());
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){

        switch (requestCode){

            case PICKER:
                if (resultCode == RESULT_OK){
                    uri = data.getData();





                    String FilePath =  data.getData().getPath();
                    curriculum = new File(data.getData().getPath());

                    ruta.setText(FilePath);

                }


        }

    }


    private void mostrarMensaje(String message) {
        Toast toast = Toast.makeText(this,message,Toast.LENGTH_LONG);
        toast.show();
    }


    private void limpiar() {

        documento.setText("");
        nombres.setText("");
        apellidos.setText("");
        contra.setText("");
        confir.setText("");
        correo.setText("");

        tipoDocu.setSelection(0);




    }




}
